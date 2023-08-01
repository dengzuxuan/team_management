package com.team.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    @GetMapping("/test")
    public Map<String,String>test(){
        Map<String,String> m1 = new HashMap<>();
        m1.put("test","success");
        return m1;
    }
}
