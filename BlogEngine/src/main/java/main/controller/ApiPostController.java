package main.controller;

import main.api.response.ApiPostListResponse;
import main.service.ApiPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final ApiPostService apiPostService;

    public ApiPostController(ApiPostService apiPostService) {
        this.apiPostService = apiPostService;
    }

    @GetMapping("/post")
    private ResponseEntity<ApiPostListResponse> posts(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "recent") String mode) {
        return ResponseEntity.ok(apiPostService.getPosts(offset, limit, mode));
    }
}
