package main.controller;

import lombok.AllArgsConstructor;
import main.api.request.LoginRequest;
import main.api.request.RegRequest;
import main.api.response.AuthCheckResponse;
import main.api.response.CaptchaResponse;
import main.api.response.LogoutResponse;
import main.api.response.RegResponse;
import main.service.AuthCheckService;
import main.service.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class ApiAuthController {
    private final AuthCheckService authCheckService;
    private final CaptchaService captchaService;

    @GetMapping("/check")
    public ResponseEntity<AuthCheckResponse> authCheck(Principal principal) {
        return ResponseEntity.ok(authCheckService.getAuthCheck(principal));
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        return ResponseEntity.ok(captchaService.getCaptchaCode());
    }

    @PostMapping("/register")
    public ResponseEntity<RegResponse> register(@RequestBody RegRequest regRequest) {
        return ResponseEntity.ok(authCheckService.getRegResponse(regRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthCheckResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authCheckService.login(loginRequest));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout() {
        return ResponseEntity.ok(authCheckService.getLogoutResponse());
    }
}
