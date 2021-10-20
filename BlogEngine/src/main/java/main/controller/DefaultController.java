package main.controller;

import lombok.AllArgsConstructor;
import main.api.response.InitResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class DefaultController {
    private final InitResponse initResponse;

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }
}
