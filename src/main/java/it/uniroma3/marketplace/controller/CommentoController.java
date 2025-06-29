// src/main/java/it/uniroma3/marketplace/controller/CommentoController.java
package it.uniroma3.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import it.uniroma3.marketplace.model.*;
import it.uniroma3.marketplace.service.*;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CommentoController {

    @Autowired private CommentoService commentoService;
    @Autowired private AnnuncioService  annuncioService;
    @Autowired private UserService      userService;

    // gestisce l’inserimento
    @PostMapping("/user/annunci/{annuncioId}/comment")
    public String add(@PathVariable Long annuncioId,
                    @RequestParam String text,
                    @RequestParam String offer) {
        User user = userService.getCurrentUser();
        if (user == null) return "redirect:/login";

        Annuncio ann = annuncioService.findById(annuncioId);
        if (ann == null) return "error";

        Commento c = new Commento();
        c.setText(text);
        c.setOffer(offer);
        c.setUser(user);
        c.setAnnuncio(ann);
        commentoService.save(c);

        // torno alla pagina /user/annunci/{id}
        return "redirect:/user/annunci/" + annuncioId;
    }

    @PostMapping("/user/annunci/{annuncioId}/comment/{id}/delete")
    public String delete(@PathVariable Long annuncioId,
                        @PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (user == null) return "redirect:/login";

        Commento c = commentoService.findById(id);
        if (c != null && c.getUser().getId().equals(user.getId())) {
            commentoService.deleteById(id);
        }
        return "redirect:/user/annunci/" + annuncioId;
    }
}
