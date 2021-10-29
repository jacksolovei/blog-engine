package main.controller;

import lombok.AllArgsConstructor;
import main.api.response.ApiPostListResponse;
import main.api.response.PostByIdResponse;
import main.model.Post;
import main.service.ApiPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiPostController {
    private final ApiPostService apiPostService;

    @GetMapping("/post")
    private ResponseEntity<ApiPostListResponse> posts(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "recent") String mode) {
        return ResponseEntity.ok(apiPostService.getPosts(offset, limit, mode));
    }

    @GetMapping("/post/search")
    private ResponseEntity<ApiPostListResponse> search(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String query) {
        return ResponseEntity.ok(apiPostService.getPostsByQuery(offset, limit, query));
    }

    @GetMapping("/post/byDate")
    private ResponseEntity<ApiPostListResponse> byDate(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam String date) {
        return ResponseEntity.ok(apiPostService.getPostsByDate(offset, limit, date));
    }

    @GetMapping("/post/byTag")
    private ResponseEntity<ApiPostListResponse> byTag(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam String tag) {
        return ResponseEntity.ok(apiPostService.getPostsByTag(offset, limit, tag));
    }

    @GetMapping("post/{id}")
    private ResponseEntity<PostByIdResponse> getById(@PathVariable int id) {
        Optional<Post> optionalPost = apiPostService.getPostById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(apiPostService.getPostResponseById(optionalPost.get()));
    }
}
