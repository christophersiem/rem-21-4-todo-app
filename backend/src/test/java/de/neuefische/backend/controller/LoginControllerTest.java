package de.neuefische.backend.controller;

import de.neuefische.backend.dto.LoginData;
import de.neuefische.backend.security.model.AppUser;
import de.neuefische.backend.security.repo.AppUserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secret=testSecret")
class LoginControllerTest {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    public void LoginControllerTestValidCredentials() {
        //GIVEN
        userRepo.save(AppUser.builder().username("test_username").password(encoder.encode("test_password")).build());

        //WHEN
        LoginData loginData = new LoginData("test_username", "test_password");

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", loginData, String.class);

        //THEN
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Claims body = Jwts.parser().setSigningKey("testSecret").parseClaimsJws(response.getBody()).getBody();
        assertEquals("test_username", body.getSubject());
    }

    @Test
    public void LoginControllerTestInvalidCredentials() {
        //GIVEN
        userRepo.save(AppUser.builder().username("test_username").password(encoder.encode("test_password")).build());

        //WHEN
        LoginData loginData = new LoginData("test_username", "test_wrong_password");

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", loginData, String.class);

        //THEN
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}