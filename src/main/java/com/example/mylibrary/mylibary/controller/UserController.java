package com.example.mylibrary.mylibary.controller;

import com.example.mylibrary.service.UserSecurityService;
import com.example.mylibrary.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSecurityService userSecurityService;

    @PostMapping("/user/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody Map<String, String> userCreateForm) {
        Map<String, Object> resultMap = userService.createUser(userCreateForm);
        return new ResponseEntity<>(resultMap, resultMap.containsKey("error") ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> resultMap = userSecurityService.authenticateUser(loginRequest);
        return new ResponseEntity<>(resultMap, resultMap.containsKey("error") ? HttpStatus.UNAUTHORIZED : HttpStatus.OK);
    }
}
