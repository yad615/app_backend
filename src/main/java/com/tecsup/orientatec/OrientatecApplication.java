package com.tecsup.orientatec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync

public class OrientatecApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrientatecApplication.class, args);
    }

}
