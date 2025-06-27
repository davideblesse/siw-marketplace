package it.uniroma3.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.marketplace.model.User;
import it.uniroma3.marketplace.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /** pagina profilo */
    @GetMapping("/user/profile")
    public String showProfile(Model model) {
        User current = userService.getCurrentUser();
        if (current == null)                      // safety-net
            return "redirect:/login";

        model.addAttribute("user", current);
        return "user/profile";                    // user/profile.html
    }
}
