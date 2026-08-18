package com.kartus.api.domain.user.controller;

import com.kartus.api.domain.user.service.UserService;
import com.kartus.api.domain.user.dto.response.UserInfoResponseDTO;
import com.kartus.api.global.dto.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("internal/users")
@RequiredArgsConstructor
public class InternalUserController {
    private final UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<GlobalApiResponse<UserInfoResponseDTO>> getUserInfo(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(GlobalApiResponse.success(userService.getUserInfo(userId)));
    }
}
