package com.kartus.api.domain.internal.health.controller;

import com.kartus.api.global.dto.GlobalApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("internal/health")
public class InternalHealthController {

    @GetMapping
    public ResponseEntity<GlobalApiResponse<Void>> health() {
        return ResponseEntity.ok(GlobalApiResponse.success("internal ok"));
    }
}
