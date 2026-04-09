package com.example.wildlife.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tactical")
public class TacticalController {

    @PostMapping("/lights")
    public String activateLights() {
        // TODO: Implement actual lights control
        System.out.println("🚨 Tactical lights activated!");
        return "Lights activated";
    }

    @PostMapping("/alarm")
    public String activateAlarm() {
        // TODO: Implement actual alarm control
        System.out.println("🚨 Tactical alarm activated!");
        return "Alarm activated";
    }
}