package com.kartus.api.domain.user;

import com.kartus.api.domain.user.dto.response.UserInfoResponseDTO;
import com.kartus.api.domain.user.entity.User;
import com.kartus.api.domain.user.error.UserErrorCode;
import com.kartus.api.domain.user.repository.UserRepository;
import com.kartus.api.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfoResponseDTO getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return new UserInfoResponseDTO(user.getId(), user.getNickname());
    }
}
