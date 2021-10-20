package main.controller;

import lombok.AllArgsConstructor;
import main.api.response.AuthCheckResponse;
import main.service.AuthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class ApiAuthController {
    private final AuthCheckService authCheckService;

    @GetMapping("/check")
    private ResponseEntity<AuthCheckResponse> authCheck() {
        return ResponseEntity.ok(authCheckService.getAuthCheck());
    }
}
