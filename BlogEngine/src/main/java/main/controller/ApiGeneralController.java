package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagListResponse;
import main.api.response.TagResponse;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagService tagService;

    public ApiGeneralController(InitResponse initResponse, SettingsService settingsService, TagService tagService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.tagService = tagService;
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }
    
    @GetMapping("/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settingsService.getGlobalSettings());
        //return new ResponseEntity<>(settingsService.getGlobalSettrings(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tag")
    private ResponseEntity<TagListResponse> tags(@RequestParam(required = false) String query) {
        if (query == null) {
            return ResponseEntity.ok(tagService.getTags());
        }
        return ResponseEntity.ok(tagService.getTagByQuery(query));
    }

    /*private SettingsResponse settings() {
        return settingsService.getGlobalSettrings();
        //return new SettingsResponse();
    }*/

    //GET api/init
    /*{
        "title": "DevPub",
            "subtitle": "Рассказы разработчиков",
            "phone": "+7 903 666-44-55",
            "email": "mail@mail.ru",
            "copyright": "Дмитрий Сергеев",
            "copyrightFrom": "2005"
    }*/

    //GET api/settings
    /*{
        "MULTIUSER_MODE": false,
            "POST_PREMODERATION": true,
            "STATISTICS_IS_PUBLIC": true
    }*/

    //GET api/tag


}
