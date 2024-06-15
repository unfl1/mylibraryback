package com.example.mylibrary.mylibary.service;

import com.example.mylibrary.mylibary.domain.SiteUser;
import com.example.mylibrary.mylibary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> createUser(Map<String, String> userCreateForm) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SiteUser user = new SiteUser();
            user.setUsername(userCreateForm.get("username"));
            user.setNickname(userCreateForm.get("nickname"));
            user.setEmail(userCreateForm.get("email"));
            user.setPassword(passwordEncoder.encode(userCreateForm.get("password1")));
            SiteUser savedUser = this.userRepository.save(user);
            resultMap.put("message", "User signed up successfully!");
            resultMap.put("user", savedUser);  // 저장된 사용자 정보를 반환
            logger.info("User signed up successfully: {}", savedUser);
        } catch (Exception e) {
            resultMap.put("error", e.getMessage());
            logger.error("Error during user sign up: ", e);
        }
        return resultMap;
    }
}