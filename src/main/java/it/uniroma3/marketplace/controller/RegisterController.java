package it.uniroma3.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.marketplace.model.Credentials;
import it.uniroma3.marketplace.service.CredentialsService;
import it.uniroma3.marketplace.service.UserService;
import jakarta.validation.Valid;


@Controller
public class RegisterController {

    @Autowired
    public CredentialsService credentialsService;
    @Autowired
    public UserService userService;

    @GetMapping("/register")
    public String getRegisterForm(Model model){
        model.addAttribute("credentials", new Credentials());
        return "register";
    }

    @PostMapping("/register")
    public String postRegisterForm(
        @Valid @ModelAttribute Credentials credentials,
        BindingResult binding,
        Model model
        ) {
        if(credentialsService.existsByUsername(credentials.getUsername())){
            binding.rejectValue("username", null, "Questo username esiste già");
        }
        if(credentialsService.existsByUsername(credentials.getUsername())){
            binding.rejectValue("user.email", null, "Questa email è già usata da un altro utente");
        }

        if(binding.hasErrors()){
            return "register";
        }
        credentials.setRole("DEFAULT");
        credentialsService.save(credentials);
        return "redirect:/login";
    }
}