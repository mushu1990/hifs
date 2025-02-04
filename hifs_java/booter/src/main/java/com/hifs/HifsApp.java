package com.hifs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Hello world!
 */
@SpringBootApplication(scanBasePackages = {"com.hifs"})
@EnableCaching
public class HifsApp {
    public static void main(String[] args) {
        SpringApplication.run(HifsApp.class, args);
        System.out.println("--------------------------hifs_java启动成功---------------------------");
    }
}
