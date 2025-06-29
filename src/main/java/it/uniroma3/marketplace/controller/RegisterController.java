package it.uniroma3.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import it.uniroma3.marketplace.model.Credentials;
import it.uniroma3.marketplace.model.User;
import it.uniroma3.marketplace.service.CredentialsService;

@Controller
public class RegisterController {

    @Autowired
    private CredentialsService credentialsService;

    /**
     * Mostra la pagina di registrazione.
     */
    @GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "register";
	}


    /**
     * Processa il form di registrazione.
    */
    @PostMapping("/register")
    public String registerUser(
        @Valid @ModelAttribute("credentials") Credentials credentials,
        BindingResult binding
    ) {
        if (credentialsService.existsByUsername(credentials.getUsername())) {
            binding.rejectValue("username","error.credentials","Username già esistente");
        }
        if (binding.hasErrors()) return "register";

        // role e user già collegati
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentialsService.save(credentials);
        return "redirect:/login?registered";
    }

}

