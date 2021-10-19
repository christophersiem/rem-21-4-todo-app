package de.neuefische.backend.controller;

import de.neuefische.backend.dto.LoginData;
import de.neuefische.backend.model.Todo;
import de.neuefische.backend.repo.TodoRepo;
import de.neuefische.backend.security.model.AppUser;
import de.neuefische.backend.security.repo.AppUserRepo;
import de.neuefische.backend.security.service.AppUserDetailService;
import de.neuefische.backend.service.IdService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.ArrayMatching.arrayContainingInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secret=testSecret")
class TodoControllerTest {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepo repository;

    @BeforeEach
    public void clearDb() {
        repository.deleteAll();
    }

    @MockBean
    private IdService idService;

    @LocalServerPort
    private int port;

    @Test
    void addTodo() {

        // GIVE
        Todo todo = new Todo(null, "Dinge tun", "OPEN");
        when(idService.generateId()).thenReturn("1");
        HttpHeaders headers = getHttpHeadersWithAuthToken();
        // WHEN
        ResponseEntity<Todo> postResponse = restTemplate.exchange("/api/todo", HttpMethod.POST, new HttpEntity<>(todo, headers), Todo.class);
        Todo actual = postResponse.getBody();

        // THEN
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(actual);
        assertEquals(new Todo("1", "Dinge tun", "OPEN"), actual);

        // THEN: check via GET if element was created
        String actualId = actual.getId();
        ResponseEntity<Todo> getResponse = restTemplate.exchange("/api/todo/" + actualId, HttpMethod.GET, new HttpEntity<>(headers), Todo.class);
        Todo persistedTodo = getResponse.getBody();

        assertNotNull(persistedTodo);
        assertEquals(persistedTodo.getId(), actualId);
        assertEquals(todo.getDescription(), persistedTodo.getDescription());
        assertEquals(todo.getStatus(), persistedTodo.getStatus());
    }

    @Test
    public void getTodoItemsShouldReturnItemsFromDb() {
        //GIVEN
        repository.save(new Todo("1", "sleep", "OPEN"));
        repository.save(new Todo("2", "chill ", "IN_PROGRESS"));
        HttpHeaders headers = getHttpHeadersWithAuthToken();

        //WHEN
        ResponseEntity<Todo[]> response = restTemplate.exchange("/api/todo", HttpMethod.GET, new HttpEntity<>(headers), Todo[].class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), arrayContainingInAnyOrder(
                new Todo("1", "sleep", "OPEN"),
                new Todo("2", "chill ", "IN_PROGRESS")));

    }

    @Test
    public void putTodoItemShouldUpdateItem() {
        //GIVEN
        repository.save(new Todo("1", "sleep", "OPEN"));
        repository.save(new Todo("2", "chill", "IN_PROGRESS"));
        HttpHeaders headers = getHttpHeadersWithAuthToken();

        //WHEN
        Todo updatedTodo = new Todo("1", "drink", "OPEN");
        restTemplate.exchange("/api/todo/1", HttpMethod.PUT, new HttpEntity<>(updatedTodo, headers), Todo.class);

        //THEN
        List<Todo> todoItems = repository.findAll();
        assertThat(todoItems, containsInAnyOrder(
                new Todo("2", "chill", "IN_PROGRESS"),
                new Todo("1", "drink", "OPEN")));
    }

    @Test
    public void getTodoShouldReturnTodoItem() {
        //GIVEN
        repository.save(new Todo("1", "sleep", "OPEN"));
        repository.save(new Todo("2", "chill", "IN_PROGRESS"));
        HttpHeaders headers = getHttpHeadersWithAuthToken();

        //WHEN
        ResponseEntity<Todo> response = restTemplate.exchange("/api/todo/2", HttpMethod.GET, new HttpEntity<>(headers), Todo.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(new Todo("2", "chill", "IN_PROGRESS")));

    }

    @Test
    public void deleteTodoShouldDeleteItemFromDb() {
        //GIVEN
        repository.save(new Todo("1", "sleep", "OPEN"));
        repository.save(new Todo("2", "chill", "IN_PROGRESS"));
        HttpHeaders headers = getHttpHeadersWithAuthToken();

        //WHEN
        restTemplate.exchange("http://localhost:" + port + "/api/todo/2", HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        //THEN
        List<Todo> todoItems = repository.findAll();
        assertEquals(List.of(new Todo("1", "sleep", "OPEN")), todoItems);
    }

    private HttpHeaders getHttpHeadersWithAuthToken() {
        userRepo.save(AppUser.builder().username("test_username").password(passwordEncoder.encode("test_password")).build());
        LoginData loginData = new LoginData("test_username", "test_password");

        ResponseEntity<String> tokenResponse = restTemplate.postForEntity("/auth/login", loginData, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getBody());
        return headers;
    }
}
