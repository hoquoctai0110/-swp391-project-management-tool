package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.GithubCommitDto;
import com.qnhu.swp391projectmanagementtool.services.interfaces.GithubService;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import com.qnhu.swp391projectmanagementtool.repositories.CommitMappingRepository;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.entities.CommitMapping;
import lombok.RequiredArgsConstructor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final CommitMappingRepository commitMappingRepository;

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

    @Override
    public List<GithubCommitDto> getIndividualCommits(String owner, String repo, String author) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits?author=%s", owner, repo, author);
        System.out.println(url);
        // Use ParameterizedTypeReference to parse JSON list correctly
        ResponseEntity<List<GithubCommitDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GithubCommitDto>>() {
                });

        return response.getBody();
    }

    @Override
    public void processGithubWebhook(Map<String, Object> payload) {
        if (!payload.containsKey("commits")) return;

        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
        String repoFullName = repository != null ? (String) repository.get("full_name") : "unknown/repo";
        String branch = (String) payload.get("ref");
        if (branch != null && branch.startsWith("refs/heads/")) {
            branch = branch.substring(11);
        }

        List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
        if (commits == null) return;

        for (Map<String, Object> commit : commits) {
            String commitHash = (String) commit.get("id");
            String message = (String) commit.get("message");
            Map<String, Object> author = (Map<String, Object>) commit.get("author");
            String githubUsername = author != null ? (String) author.get("username") : null;
            String githubEmail = author != null ? (String) author.get("email") : null;
            
            // Extract Jira Key
            String jiraKey = extractJiraKey(message);

            // Find User
            User user = null;
            if (githubUsername != null) {
                user = userRepository.findByGithubAccountId(githubUsername).orElse(null);
            }
            if (user == null && githubEmail != null) {
                user = userRepository.findByEmail(githubEmail).orElse(null);
            }

            // Create Mapping
            CommitMapping mapping = commitMappingRepository.findById(commitHash).orElse(new CommitMapping());
            mapping.setCommitHash(commitHash);
            mapping.setMessage(message);
            mapping.setRepository(repoFullName);
            mapping.setBranch(branch);
            mapping.setJiraIssueKey(jiraKey);
            mapping.setUser(user);
            
            String timestamp = (String) commit.get("timestamp");
            if (timestamp != null) {
                try {
                    mapping.setCommitDate(java.time.OffsetDateTime.parse(timestamp).toLocalDateTime());
                } catch (Exception e) {}
            }
            
            commitMappingRepository.save(mapping);
        }
    }

    private String extractJiraKey(String message) {
        if (message == null) return null;
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("([A-Z]+-[0-9]+)");
        java.util.regex.Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
