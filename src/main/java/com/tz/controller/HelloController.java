package com.tz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lhucstart
 * @date 2022-11-08 15:54
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Map<String, Object> hello(String name, Integer age) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        return map;
    }
}
