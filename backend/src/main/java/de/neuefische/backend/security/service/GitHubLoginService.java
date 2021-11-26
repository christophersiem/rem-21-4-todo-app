package de.neuefische.backend.security.service;

import de.neuefische.backend.security.api.GitHubApiService;
import de.neuefische.backend.security.model.GitHubUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class GitHubLoginService {

    private final GitHubApiService gitHubApiService;
    private final JWTUtilService jwtUtilService;

    public String verifyGitHubCode(String code) {
        // 1) Verify code via GitHub
        String gitHubToken = gitHubApiService.retrieveGitHubToken(code);

        // 2) Retrieve User Info
        GitHubUserDto gitHubUserDto = gitHubApiService.retrieveUserInfo(gitHubToken);

        // 3) Create JWT access token
        return jwtUtilService.createToken(new HashMap<>(), gitHubUserDto.getLogin());
    }
}
