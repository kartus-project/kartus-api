package com.kartus.api.domain.health.controller;

import com.kartus.api.global.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/health")
public class HealthController {
    @GetMapping
    public String health() {
        return "Server is running";
    }

    @GetMapping("test")
    public Long test(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userPrincipal.getUserId();
    }
}
