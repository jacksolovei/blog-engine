package main.controller;

import lombok.AllArgsConstructor;
import main.api.request.RegRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.CaptchaResponse;
import main.api.response.RegResponse;
import main.service.AuthCheckService;
import main.service.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class ApiAuthController {
    private final AuthCheckService authCheckService;
    private final CaptchaService captchaService;

    @GetMapping("/check")
    private ResponseEntity<AuthCheckResponse> authCheck() {
        return ResponseEntity.ok(authCheckService.getAuthCheck());
    }

    @GetMapping("/captcha")
    private ResponseEntity<CaptchaResponse> getCaptcha() {
        return ResponseEntity.ok(captchaService.getCaptchaCode());
    }

    @PostMapping("/register")
    private ResponseEntity<RegResponse> register(@RequestBody RegRequest regRequest) {
        return ResponseEntity.ok(authCheckService.getRegResponse(regRequest));
    }
}
