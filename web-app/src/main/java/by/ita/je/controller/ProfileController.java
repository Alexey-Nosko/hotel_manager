package by.ita.je.controller;

import by.ita.je.models.Profile;
import by.ita.je.service.WebAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final WebAppService webAppService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/reg")
    public String reg() {
        return "reg";
    }

    @PostMapping("/reg")
    public String addProfile(@ModelAttribute("profile") Profile profile) {

        webAppService.clientRegistration(profile);

        return "redirect:/login";
    }



}
