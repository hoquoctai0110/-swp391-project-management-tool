package com.qnhu.swp391projectmanagementtool.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JiraProperties {

    @Value("${jira.api.url}")
    private String url;

    @Value("${jira.api.email}")
    private String email;

    @Value("${jira.api.token}")
    private String token;

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}