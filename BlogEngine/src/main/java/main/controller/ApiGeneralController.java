package main.controller;

import lombok.AllArgsConstructor;
import main.api.request.CommentRequest;
import main.api.response.*;
import main.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiGeneralController {
    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final ImageService imageService;
    private final CommentService commentService;

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settingsService.getGlobalSettings());
    }

    @GetMapping("/tag")
    public ResponseEntity<TagListResponse> tags(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(tagService.getTags(query));
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam(required = false) String year) {
        return ResponseEntity.ok(calendarService.getPostsInCalendar(year));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> image(@RequestParam MultipartFile image) {
        if (!imageService.checkImage(image)) {
            return ResponseEntity.badRequest().body(imageService.getErrorResponse(image));
        }
        return ResponseEntity.ok(imageService.uploadImage(image));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/comment")
    public ResponseEntity<?> postComment(@RequestBody CommentRequest commentRequest,
                                         Principal principal) {
        if (!commentService.checkComment(commentRequest)) {
            return ResponseEntity.badRequest()
                    .body(commentService.getErrorResponse(commentRequest));
        }
        return ResponseEntity.ok(commentService.saveComment(commentRequest, principal));
    }
}
