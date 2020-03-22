package com.taskagile.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.taskagile")
public class TaskAgileApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskAgileApplication.class, args);
    }

}
