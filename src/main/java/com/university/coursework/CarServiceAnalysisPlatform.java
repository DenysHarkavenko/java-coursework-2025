package com.university.coursework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CarServiceAnalysisPlatform {

    public static void main(String[] args) {
        SpringApplication.run(CarServiceAnalysisPlatform.class, args);
    }
}