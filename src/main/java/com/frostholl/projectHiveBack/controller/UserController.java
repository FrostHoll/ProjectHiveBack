package com.frostholl.projectHiveBack.controller;

import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.response.UserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    public ResponseEntity<UserDetailsResponse> getUserDetails(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserDetailsResponse
                .builder()
                .login(user.getLogin())
                .fullName(user.getFullName())
                .build()
        );
    }
}
