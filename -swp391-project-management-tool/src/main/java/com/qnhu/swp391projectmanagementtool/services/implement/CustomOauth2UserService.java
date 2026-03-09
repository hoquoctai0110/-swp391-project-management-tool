package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Value("${app.security.admin-email}")
    private String adminEmail;

    @Value("${app.security.lecturer-emails}")
    private String lecturerEmail;

    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");

        if (email == null) {
            Object loginObj = attributes.get("login");
            if (loginObj != null) {
                email = loginObj.toString() + "@github.com";
            } else {
                Object idObj = attributes.get("id");
                String id = idObj != null ? idObj.toString() : "unknown";
                email = id + "@oauth2.internal";
            }
        }

        String name = (String) attributes.get("name");
        if (name == null) {
            Object loginObj = attributes.get("login");
            if (loginObj != null) {
                name = loginObj.toString();
            } else {
                name = email;
            }
        }

        String finalEmail = email;
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setEmail(finalEmail);
        user.setUsername(name);

        Role role = determineRoleByEmail(finalEmail);
        user.setRole(role);

        userRepository.save(user);
        return oauth2User;
    }

    private Role determineRoleByEmail(String email) {
        if (email == null)
            return Role.ROLE_MEMBER;

        if (email.equalsIgnoreCase(adminEmail)) {
            return Role.ROLE_ADMIN;
        }

        if (email.equalsIgnoreCase(lecturerEmail)) {
            return Role.ROLE_LECTURER;
        }

        if (email.toLowerCase().endsWith("@fe.edu.vn")) {
            return Role.ROLE_LECTURER;
        }

        if (email.toLowerCase().endsWith("@fpt.edu.vn")) {
            return Role.ROLE_MEMBER;
        }

        return Role.ROLE_MEMBER;
    }
}