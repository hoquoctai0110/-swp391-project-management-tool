package com.qnhu.swp391projectmanagementtool.services.implement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qnhu.swp391projectmanagementtool.config.JiraProperties;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JiraServiceImpl implements JiraService {

    private final JiraProperties jiraProperties;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean testConnection() {

        String url = jiraProperties.getUrl() + "/rest/api/3/myself";

        HttpHeaders headers = buildHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    private HttpHeaders buildHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");

        return headers;
    }

    private String buildAuthHeader() {

        String auth = jiraProperties.getEmail() + ":" + jiraProperties.getToken();

        String encoded =
                Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        return "Basic " + encoded;
    }

    @Override
    public void createProjectForGroup(int groupId, String projectKey, String projectName) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getProjectKey() != null) {
            throw new RuntimeException("Group already has Jira project");
        }

        String url = jiraProperties.getUrl() + "/rest/api/3/project";

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

        HttpEntity<String> entity = new HttpEntity<>(body, buildHeaders());

        try {

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {

                group.setProjectKey(projectKey);
                groupRepository.save(group);

            } else {

                throw new RuntimeException("Failed to create Jira project");
            }

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Create project failed", e);
        }
    }

    private String getMyAccountId() {

        String url = jiraProperties.getUrl() + "/rest/api/3/myself";

        HttpEntity<String> entity = new HttpEntity<>(buildHeaders());

        try {

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());

            return root.get("accountId").asText();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void syncUserToJiraProject(Group group, User user) {

        if (group.getProjectKey() == null) {
            throw new RuntimeException("ProjectKey null");
        }

        if (user.getRole() == Role.ROLE_ADMIN) {
            return;
        }

        try {

            String jiraRoleName = mapSystemRoleToJiraRole(user.getRole());

            String accountId = user.getJiraAccountId();

            if (accountId == null) {

                accountId = findAccountId(user.getEmail());

                if (accountId == null) {
                    throw new RuntimeException("Cannot find Jira user: " + user.getEmail());
                }

                user.setJiraAccountId(accountId);
                userRepository.save(user);
            }

            String roleId = getProjectRoleId(group.getProjectKey(), jiraRoleName);

            addUserToProjectRole(group.getProjectKey(), roleId, accountId);

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Sync user failed", e);
        }
    }

    private String mapSystemRoleToJiraRole(Role role) {

        return switch (role) {

            case ROLE_LEADER, ROLE_MEMBER, ROLE_LECTURER -> "Administrators";

            default -> "Users";
        };
    }

    /**
     * FIX: search user by query (NOT email)
     */
    private String findAccountId(String keyword) {

        try {

            String url = jiraProperties.getUrl()
                    + "/rest/api/3/user/search?query=" + keyword;

            HttpEntity<String> entity = new HttpEntity<>(buildHeaders());

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode arr = objectMapper.readTree(response.getBody());

            if (arr.isEmpty()) {
                return null;
            }

            for (JsonNode user : arr) {

                if (user.has("emailAddress")) {

                    String email = user.get("emailAddress").asText();

                    if (email.equalsIgnoreCase(keyword)) {

                        return user.get("accountId").asText();
                    }
                }
            }

            /**
             * fallback nếu email bị ẩn
             */
            return arr.get(0).get("accountId").asText();

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private String getProjectRoleId(String projectKey, String roleName) {

        try {

            String url = jiraProperties.getUrl()
                    + "/rest/api/3/project/" + projectKey + "/role";

            HttpEntity<String> entity = new HttpEntity<>(buildHeaders());

            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());

            if (!root.has(roleName)) {
                return null;
            }

            String roleUrl = root.get(roleName).asText();

            return roleUrl.substring(roleUrl.lastIndexOf("/") + 1);

        } catch (Exception e) {

            throw new RuntimeException("Get roleId failed", e);
        }
    }

    private void addUserToProjectRole(String projectKey, String roleId, String accountId) {

        try {

            String url = jiraProperties.getUrl()
                    + "/rest/api/3/project/" + projectKey + "/role/" + roleId;

            String body = """
            {
               "user": ["%s"]
            }
            """.formatted(accountId);

            HttpEntity<String> entity =
                    new HttpEntity<>(body, buildHeaders());

            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        } catch (Exception e) {

            throw new RuntimeException("Add user to role failed", e);
        }
    }
}