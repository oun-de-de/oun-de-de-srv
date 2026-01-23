package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.GIT_SHA;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfraController {
    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok(
            String.format("I'm healthy, git_sha: %s", System.getenv(GIT_SHA))
        );
    }
}
