package de.neuefische.backend.controller;

import de.neuefische.backend.model.Todo;
import de.neuefische.backend.repo.TodoRepo;
import de.neuefische.backend.security.model.AppUser;
import de.neuefische.backend.security.repo.AppUserRepo;
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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.ArrayMatching.arrayContainingInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        HttpHeaders headers = getHttpHeadersWithJWT();

        // GIVE
        Todo todo = new Todo(null, "Dinge tun", "OPEN");
        when(idService.generateId()).thenReturn("1");
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
        assertEquals(actualId, persistedTodo.getId());
        assertEquals(todo.getDescription(), persistedTodo.getDescription());
        assertEquals(todo.getStatus(), persistedTodo.getStatus());
    }

    @Test
    public void getTodoItemsShouldReturnItemsFromDb() {
        //GIVEN
        repository.save(new Todo("1", "sleep", "OPEN"));
        repository.save(new Todo("2", "chill ", "IN_PROGRESS"));

        //WHEN
        ResponseEntity<Todo[]> response = restTemplate.exchange("/api/todo", HttpMethod.GET, new HttpEntity<>(getHttpHeadersWithJWT()), Todo[].class);

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

        //WHEN
        Todo updatedTodo = new Todo("1", "drink", "OPEN");
        restTemplate.exchange("/api/todo/1", HttpMethod.PUT, new HttpEntity<>(updatedTodo, getHttpHeadersWithJWT()), Todo.class);

        //THEN
        List<Todo> todoItems = repository.findAll();
        assertThat(todoItems, containsInAnyOrder(
                new Todo("2", "chill", "IN_PROGRESS"),
                new Todo("1", "drink", "OPEN")));
    }

    @Test
    public void putTodoItemShouldThrowException() {
        //GIVEN

        //WHEN
        Todo updatedTodo = new Todo("2", "drink", "OPEN");
        ResponseEntity<Todo> response = restTemplate.exchange("/api/todo/1", HttpMethod.PUT, new HttpEntity<>(updatedTodo, getHttpHeadersWithJWT()), Todo.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @Test
    public void getTodoShouldReturnTodoItem() {
        //GIVEN
        repository.save(new Todo("1", "sleep", "OPEN"));
        repository.save(new Todo("2", "chill", "IN_PROGRESS"));

        //WHEN
        ResponseEntity<Todo> response = restTemplate.exchange("/api/todo/2", HttpMethod.GET, new HttpEntity<>(getHttpHeadersWithJWT()), Todo.class);

        //THEN
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(new Todo("2", "chill", "IN_PROGRESS")));

    }

    @Test
    public void deleteTodoShouldDeleteItemFromDb() {
        //GIVEN
        repository.save(new Todo("1", "sleep", "OPEN"));
        repository.save(new Todo("2", "chill", "IN_PROGRESS"));

        //WHEN
        restTemplate.exchange("http://localhost:" + port + "/api/todo/2", HttpMethod.DELETE, new HttpEntity<>(getHttpHeadersWithJWT()), Void.class);

        //THEN
        List<Todo> todoItems = repository.findAll();
        assertEquals(todoItems, List.of(new Todo("1", "sleep", "OPEN")));
    }

    private HttpHeaders getHttpHeadersWithJWT() {
        userRepo.save(AppUser.builder().username("test_username").password(passwordEncoder.encode("some-password")).build());
        AppUser loginData = new AppUser("test_username", "some-password");
        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", loginData, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody());
        return headers;
    }

}
