package ru.nobody;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        log.info("App starting");
        SpringApplication.run(Application.class, args);
        log.info("App started");
    }
}
