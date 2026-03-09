package com.qnhu.swp391projectmanagementtool.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Value("${app.security.admin-email}")
    private String adminEmail;

    @Value("${app.security.lecturer-emails:}")
    private String lecturerEmailsConfig;

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        String email = null;
        String name = null;

        if (principal instanceof DefaultOAuth2User oauthUser) {
            Object emailObj = oauthUser.getAttributes().get("email");
            Object nameObj = oauthUser.getAttributes().get("name");

            if (emailObj != null) {
                email = emailObj.toString();
            } else {
                Object loginObj = oauthUser.getAttributes().get("login");
                if (loginObj != null) {
                    email = loginObj.toString() + "@github.com";
                } else {
                    Object idObj = oauthUser.getAttributes().get("id");
                    if (idObj != null) {
                        email = idObj.toString() + "@oauth2.internal";
                    }
                }
            }

            if (nameObj != null) {
                name = nameObj.toString();
            } else {
                Object loginObj = oauthUser.getAttributes().get("login");
                if (loginObj != null) {
                    name = loginObj.toString();
                } else {
                    name = email;
                }
            }
        }

        if (email == null)
            email = authentication.getName() + "@oauth2.internal";
        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not provided");
            return;
        }

        email = email.trim().toLowerCase();
        Optional<User> maybeUser = userRepository.findByEmail(email);
        User user;

        Set<String> lecturerEmails = new HashSet<>();
        if (lecturerEmailsConfig != null && !lecturerEmailsConfig.isBlank()) {
            for (String s : lecturerEmailsConfig.split(",")) {
                lecturerEmails.add(s.trim().toLowerCase());
            }
        }

        if (maybeUser.isPresent()) {
            user = maybeUser.get();
            logger.info("Existing user logged in: {}", email);
        } else {
            user = new User();
            user.setEmail(email);
            user.setUsername(name != null && !name.isBlank() ? name : email);

            if (email.equalsIgnoreCase(adminEmail)) {
                user.setRole(Role.ROLE_ADMIN);
            } else if (lecturerEmails.contains(email)) {
                user.setRole(Role.ROLE_LECTURER);
            } else {
                user.setRole(Role.ROLE_MEMBER);
            }

            userRepository.save(user);
            logger.info("Created new user {} with role {}", email, user.getRole().name());
        }

        String role = (user.getRole() != null) ? user.getRole().name() : Role.ROLE_MEMBER.name();
        String token = jwtUtil.generateToken(email, role);

        // Tạo HttpOnly Cookie chứa Token để bảo mật (chống XSS)
        jakarta.servlet.http.Cookie tokenCookie = new jakarta.servlet.http.Cookie("auth_token", token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(false); // Nếu chạy thực tế có HTTPS thì đổi thành true
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(24 * 60 * 60); // 1 ngày

        response.addCookie(tokenCookie);

        // Chuyển hướng về Frontend, không truyền token lộ liễu trên URL nữa
        String frontendUrl = "http://localhost:5173/";
        response.sendRedirect(frontendUrl);
    }
}