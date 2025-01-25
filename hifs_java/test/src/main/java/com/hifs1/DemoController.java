package com.hifs1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/greeting")
    public String greet() {
        return helloService.getMessage();
        //return "Hello, Actuator!";
    }
}