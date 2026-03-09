package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.config.JiraProperties;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JiraServiceImpl implements JiraService {

    private final JiraProperties jiraProperties;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean testConnection() {

        String url = jiraProperties.getUrl() + "/rest/api/3/myself";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            return false;
        }
    }

    private String buildAuthHeader() {
        String auth = jiraProperties.getEmail() + ":" + jiraProperties.getToken();
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes());
        return "Basic " + encoded;
    }

    @Override
    public void createProjectForGroup(int groupId, String projectKey, String projectName) {
        Group group = groupRepository.findById(Integer.valueOf(groupId))
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getProjectKey() != null) {
            throw new RuntimeException("Group already has a Jira project");
        }

        String url = jiraProperties.getUrl() + "/rest/api/3/project";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String leadAccountId = getMyAccountId();

        String body = """
                {
                  "key": "%s",
                  "name": "%s",
                  "projectTypeKey": "software",
                  "projectTemplateKey": "com.pyxis.greenhopper.jira:gh-scrum-template",
                  "leadAccountId": "%s"
                }
                """.formatted(projectKey, projectName, leadAccountId);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                group.setProjectKey(projectKey);
                groupRepository.save(group);
            }

        } catch (Exception ignored) {
        }
    }

    private String getMyAccountId() {

        String url = jiraProperties.getUrl() + "/rest/api/3/myself";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.get("accountId").asText();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void syncUserToJiraProject(Group group, User user) {

        if (group.getProjectKey() == null || group.getProjectKey().isBlank()) {
            return;
        }

        if (user.getRole() == Role.ROLE_ADMIN) {
            return;
        }

        try {

            String jiraRoleName = mapSystemRoleToJiraRole(user.getRole());

            String accountId = user.getJiraAccountId();
            if (accountId == null || accountId.isBlank()) {
                accountId = findAccountIdByEmail(user.getEmail());

                if (accountId == null) {
                    return;
                }

                user.setJiraAccountId(accountId);
                userRepository.save(user);
            }

            String roleId = getProjectRoleId(group.getProjectKey(), jiraRoleName);

            if (roleId != null) {
                addUserToProjectRole(group.getProjectKey(), roleId, accountId);
            }

        } catch (Exception ignored) {
        }
    }

    private String mapSystemRoleToJiraRole(Role role) {
        return switch (role) {
            case ROLE_LECTURER -> "Users";
            case ROLE_LEADER, ROLE_MEMBER -> "Developers";
            default -> "Users";
        };
    }

    private String findAccountIdByEmail(String email) {

        String url = jiraProperties.getUrl()
                + "/rest/api/3/user/search?query={email}";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, email);

            JsonNode arr = objectMapper.readTree(response.getBody());

            if (!arr.isArray() || arr.size() == 0) {
                return null;
            }

            return arr.get(0).get("accountId").asText();

        } catch (Exception e) {
            return null; // Soft mode
        }
    }

    private String encode(String s) {
        try {
            return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            return s;
        }
    }

    private String getProjectRoleId(String projectKey, String roleName) {

        String url = jiraProperties.getUrl()
                + "/rest/api/3/project/" + projectKey + "/role";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());

            if (!root.has(roleName))
                return null;

            String roleUrl = root.get(roleName).asText();
            return roleUrl.substring(roleUrl.lastIndexOf("/") + 1);

        } catch (Exception e) {
            return null;
        }
    }

    private void addUserToProjectRole(String projectKey, String roleId, String accountId) {

        String url = jiraProperties.getUrl()
                + "/rest/api/3/project/" + projectKey + "/role/" + roleId;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
                { "user": ["%s"] }
                """.formatted(accountId);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception ignored) {
            // Soft mode
        }
    }

    @Override
    public String getAccountIdByEmail(String email) {
        return findAccountIdByEmail(email);
    }
}