package com.jes.api.domain.auth.service;

import com.jes.api.domain.auth.dto.request.LoginRequestDTO;
import com.jes.api.domain.auth.dto.request.RefreshRequestDTO;
import com.jes.api.domain.auth.dto.request.SignupRequestDTO;
import com.jes.api.domain.auth.dto.response.AuthResponseDTO;
import com.jes.api.domain.auth.entity.RefreshToken;
import com.jes.api.domain.auth.error.AuthErrorCode;
import com.jes.api.domain.user.entity.User;
import com.jes.api.domain.user.error.UserErrorCode;
import com.jes.api.domain.user.repository.UserRepository;
import com.jes.api.global.exception.CustomException;
import com.jes.api.global.security.UserPrincipal;
import com.jes.api.global.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional
    public void signup(SignupRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new CustomException(UserErrorCode.DUPLICATE_USERNAME);
        }

        User user = new User(dto.username(), passwordEncoder.encode(dto.password()));

        userRepository.save(user);
    }

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        UserPrincipal userPrincipal = Objects.requireNonNull((UserPrincipal) authentication.getPrincipal());

        String accessToken = tokenProvider.createToken(userPrincipal.getUserId());
        String refreshToken = tokenProvider.createRefreshToken(userPrincipal.getUserId());

        User user = userRepository.findById(userPrincipal.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        refreshTokenService.saveOrUpdate(user, refreshToken);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponseDTO refresh(RefreshRequestDTO dto) {
        if (!tokenProvider.validateToken(dto.refreshToken())) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN);
        }

        RefreshToken rf = refreshTokenService.getValidRefreshToken(dto.refreshToken());

        Long userId = rf.getUser().getId();
        String accessToken = tokenProvider.createToken(userId);
        String refreshToken = tokenProvider.createRefreshToken(userId);

        refreshTokenService.saveOrUpdate(rf.getUser(), refreshToken);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @Transactional
    public void logout(UserPrincipal userPrincipal) {
        refreshTokenService.deleteByUserId(userPrincipal.getUserId());
    }
}
