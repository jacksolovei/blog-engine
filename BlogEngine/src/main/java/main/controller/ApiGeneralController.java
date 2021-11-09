package main.controller;

import lombok.AllArgsConstructor;
import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagListResponse;
import main.service.ApiPostService;
import main.service.CalendarService;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiGeneralController {
    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagService tagService;
    private final CalendarService calendarService;

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
}
