package com.qnhu.swp391projectmanagementtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Swp391ProjectManagementToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(Swp391ProjectManagementToolApplication.class, args);
        printStartupInfo();
    }

    private static void printStartupInfo() {
        System.out.println("""
                Server: http://localhost:8080
                Swagger: http://localhost:8080/swagger-ui.html
                """);
    }
}
