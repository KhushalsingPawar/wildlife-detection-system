package com.example.wildlife.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tactical")
public class TacticalController {

    @PostMapping("/lights")
    public String activateLights() {
        return "Lights activated";
    }

    @PostMapping("/alarm")
    public String activateAlarm() {
        return "Alarm activated";
    }
}