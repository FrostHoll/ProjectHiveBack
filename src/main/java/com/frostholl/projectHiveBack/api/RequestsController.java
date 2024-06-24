package com.frostholl.projectHiveBack.api;

import com.frostholl.projectHiveBack.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class RequestsController {

    @GetMapping("/test")
    public ResponseEntity<String> test(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

    @GetMapping()
    public ResponseEntity<String> test1() {
        return ResponseEntity.ok("Hello world!");
    }
}
