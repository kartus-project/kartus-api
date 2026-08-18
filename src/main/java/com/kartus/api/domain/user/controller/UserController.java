package com.kartus.api.domain.user.controller;

import com.kartus.api.domain.user.dto.response.UserInfoResponseDTO;
import com.kartus.api.domain.user.service.UserService;
import com.kartus.api.global.dto.GlobalApiResponse;
import com.kartus.api.global.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("me")
    public ResponseEntity<GlobalApiResponse<UserInfoResponseDTO>> getUserInfo(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ResponseEntity.ok(GlobalApiResponse.success(userService.getUserInfo(principal.getUserId())));
    }
}
