package com.hifs1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication(scanBasePackages = {"com.hifs"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
