package it.uniroma3.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.marketplace.model.Credentials;
import it.uniroma3.marketplace.model.User;
import it.uniroma3.marketplace.service.CredentialsService;
import it.uniroma3.marketplace.service.UserService;
import jakarta.validation.Valid;


@Controller
public class AuthController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
	public String showLogin(@RequestParam(value = "error", required = false) boolean error, Model model) {
        if (error)
            model.addAttribute("msgError", "Username o password errati"); 
        return "login";
    }

    
    
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "register";
	}

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                             BindingResult userBindingResult,
                             @Valid @ModelAttribute("credentials") Credentials credentials,
                             BindingResult credentialsBindingResult,
                             Model model) {
        if (credentialsBindingResult.hasErrors()) {
            return "register";
        }
        if (userBindingResult.hasErrors()) {
            return "register";
        }
        userService.saveUser(user);
        credentials.setUser(user);
        credentialsService.saveCredentials(credentials);
        return "redirect:/user/annunci";

    }
}
