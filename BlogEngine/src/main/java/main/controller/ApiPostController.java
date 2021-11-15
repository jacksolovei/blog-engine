package main.controller;

import lombok.AllArgsConstructor;
import main.api.request.PostRequest;
import main.api.response.ApiPostListResponse;
import main.api.response.PostByIdResponse;
import main.api.response.PostForModerationListResponse;
import main.api.response.RegResponse;
import main.model.Post;
import main.service.ApiPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiPostController {
    private final ApiPostService apiPostService;

    @GetMapping("/post")
    public ResponseEntity<ApiPostListResponse> posts(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "recent") String mode) {
        return ResponseEntity.ok(apiPostService.getPosts(offset, limit, mode));
    }

    @GetMapping("/post/search")
    public ResponseEntity<ApiPostListResponse> search(
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
    public ResponseEntity<ApiPostListResponse> byTag(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam String tag) {
        return ResponseEntity.ok(apiPostService.getPostsByTag(offset, limit, tag));
    }

    @GetMapping("post/{id}")
    public ResponseEntity<PostByIdResponse> getById(@PathVariable int id, Principal principal) {
        Optional<Post> optionalPost = apiPostService.getPostById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(apiPostService.getPostResponseById(optionalPost.get(), principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("post/my")
    public ResponseEntity<ApiPostListResponse> getPostByAuthUser(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "published") String status, Principal principal) {
        return ResponseEntity.ok(apiPostService.getPostsByStatus(offset, limit, status, principal));
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @GetMapping("post/moderation")
    public ResponseEntity<PostForModerationListResponse> getPostsForModeration(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "new") String status,
            Principal principal) {
        return ResponseEntity.ok(apiPostService.getPostsForModeration(offset, limit, status, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/post")
    public ResponseEntity<RegResponse> savePost(@RequestBody PostRequest postRequest,
                                                Principal principal) {
        return ResponseEntity.ok(apiPostService.savePost(postRequest, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/post/{id}")
    public ResponseEntity<RegResponse> editPost(@PathVariable int id,
                                                @RequestBody PostRequest postRequest,
                                                Principal principal) {
        Optional<Post> optionalPost = apiPostService.getPostById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(apiPostService.editPost(optionalPost.get(), postRequest, principal));
    }
}
