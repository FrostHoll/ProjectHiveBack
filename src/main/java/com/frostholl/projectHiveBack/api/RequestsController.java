package com.frostholl.projectHiveBack.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RequestsController {

    @Value("${app-version}")
    private String version;

    @GetMapping()
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("==ProjectHive Backend== Version: " + version);
    }
}
