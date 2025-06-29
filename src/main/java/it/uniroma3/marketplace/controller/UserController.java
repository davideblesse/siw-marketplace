package it.uniroma3.marketplace.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.marketplace.model.User;
import it.uniroma3.marketplace.service.CredentialsService;
import it.uniroma3.marketplace.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CredentialsService credentialsService;

    private boolean verifyId(Long idUrl, Long idUser) {
		return idUser!= null && Objects.equals(idUrl, idUser);
	}

    @GetMapping("/user/{id}")
    public String showProfile(@PathVariable("id") Long id,
            @RequestParam(value="showPasswordModal", required = false, defaultValue = "false") boolean showPasswordModal,
            Model model) {
        User user = userService.getCurrentUser();
        if (!verifyId(id, user.getId())){
            return "redirect:/login";
        }

        
        model.addAttribute("showPasswordModal", showPasswordModal);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/admin/{id}")
    public String showAdminProfile(@PathVariable("id") Long id,
            @RequestParam(value="showPasswordModal", required = false, defaultValue = "false") boolean showPasswordModal,
            Model model) {
        User user = userService.getCurrentUser();
        if (!verifyId(id, user.getId())){
            return "redirect:/login";
        }
        model.addAttribute("showPasswordModal", showPasswordModal);
        model.addAttribute("user", user);
        return "admin/profile";
    }
}
