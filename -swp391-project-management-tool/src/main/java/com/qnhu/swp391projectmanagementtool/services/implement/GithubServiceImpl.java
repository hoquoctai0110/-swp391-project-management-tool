package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.GithubCommitDto;
import com.qnhu.swp391projectmanagementtool.services.interfaces.GithubService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GithubServiceImpl implements GithubService {

    private final RestTemplate restTemplate;

    public GithubServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<GithubCommitDto> getRepositoryCommits(String owner, String repo) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits", owner, repo);

        // Use ParameterizedTypeReference to parse JSON list correctly
        ResponseEntity<List<GithubCommitDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GithubCommitDto>>() {
                });

        return response.getBody();
    }
}
