package main.controller;

import main.api.response.AuthCheckResponse;
import main.service.AuthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    private final AuthCheckService authCheckService;

    public ApiAuthController(AuthCheckService authCheckService) {
        this.authCheckService = authCheckService;
    }


    //GET api/auth/check
    /*{
        "result": true,
            "user": {
        "id": 576,
                "name": "Дмитрий Петров",
                "photo": "/avatars/ab/cd/ef/52461.jpg",
                "email": "petrov@petroff.ru",
                "moderation": true,
                "moderationCount": 56,
                "settings": true
    }
    }
    {"result": false}*/

    @GetMapping("/check")
    private ResponseEntity<AuthCheckResponse> authCheck() {
        return ResponseEntity.ok(authCheckService.getAuthCheck());
    }


}
