package com.qnhu.swp391projectmanagementtool.config;

import com.qnhu.swp391projectmanagementtool.services.implement.CustomOauth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        private final CustomOauth2UserService oAuth2UserService;
        private final OAuth2LoginSuccessHandler successHandler;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(CustomOauth2UserService oAuth2UserService,
                        OAuth2LoginSuccessHandler successHandler,
                        JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.oAuth2UserService = oAuth2UserService;
                this.successHandler = successHandler;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(

                                                                "/",
                                                                "/error",

                                                                "/api/auth/**", // login API
                                                                "/oauth2/**", // OAuth2 endpoint

                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html",
                                                                "/swagger-resources/**",
                                                                "/webjars/**",
                                                                "/v2/api-docs",
                                                                "/v3/api-docs.yaml",

                                                                "/api/admin/**",
                                                                "/api/jira/test",
                                                                "/api/jira/account-id"

                                                ).permitAll()

                                                .anyRequest().authenticated())

                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                                                .successHandler(successHandler));

                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}