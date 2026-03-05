package com.kartus.api.global.security;

import com.kartus.api.domain.user.entity.User;
import com.kartus.api.domain.user.error.UserErrorCode;
import com.kartus.api.domain.user.repository.UserRepository;
import com.kartus.api.global.exception.CustomException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws CustomException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        return new UserPrincipal(user);
    }

    public UserPrincipal loadUserById(String userId) throws CustomException {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        return new UserPrincipal(user);
    }
}
