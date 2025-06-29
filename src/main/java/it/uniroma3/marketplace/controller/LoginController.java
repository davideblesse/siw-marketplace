package it.uniroma3.marketplace.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam Optional<String> error, Model model) {
        model.addAttribute("loginError", error.isPresent());
        return "login";
    }
}

