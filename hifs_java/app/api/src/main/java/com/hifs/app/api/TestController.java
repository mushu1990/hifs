package com.hifs.app.api;


import com.hifs.app.system.core.security.annotation.Anonymous;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Autowired
    private Enforcer enforcer;


    @Anonymous
    @GetMapping("/hello")
    public String greet() {
        enforcer.addPolicy("user1", "/data11", "read");
        return "a";
    }

}
