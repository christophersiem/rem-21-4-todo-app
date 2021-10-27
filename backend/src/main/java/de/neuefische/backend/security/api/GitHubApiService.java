package de.neuefische.backend.security.api;

import de.neuefische.backend.controller.exception.GitHubAuthException;
import de.neuefische.backend.security.model.GitHubAccessTokenDto;
import de.neuefische.backend.security.model.GitHubOAuthCredentialsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class GitHubApiService {

    private RestTemplate restTemplate;

    private static final String GITHUB_CODE_URL = "https://github.com/login/oauth/access_token";

    @Value("${de.neuefische.todo.github.clientid}")
    private String clientId;

    @Value("${de.neuefische.todo.github.clientsecret}")
    private String clientSecret;

    public GitHubApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String retrieveGitHubToken(String code) {

        GitHubOAuthCredentialsDto credentialsDto = GitHubOAuthCredentialsDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<GitHubAccessTokenDto> response = restTemplate.exchange(
                GITHUB_CODE_URL,
                HttpMethod.POST,
                new HttpEntity<>(credentialsDto, httpHeaders),
                GitHubAccessTokenDto.class);

        if(response.getBody() == null){
            throw new GitHubAuthException("Error while authenticating with code via GitHub!");
        }

        return response.getBody().getAccessToken();

    }
}
