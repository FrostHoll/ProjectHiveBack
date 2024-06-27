package com.frostholl.projectHiveBack.controller;

import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.response.UserDetailsResponse;
import com.frostholl.projectHiveBack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService service;

    @GetMapping("/user/{userLogin}")
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable("userLogin") String userLogin) {
        var user = service.getUserByLogin(userLogin);
        return ResponseEntity.ok(UserDetailsResponse
                .builder()
                .login(user.getLogin())
                .fullName(user.getFullName())
                .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponse> getUserDetails(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserDetailsResponse
                .builder()
                .login(user.getLogin())
                .fullName(user.getFullName())
                .build()
        );
    }
}
