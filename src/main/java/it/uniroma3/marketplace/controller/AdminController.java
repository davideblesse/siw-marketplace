package it.uniroma3.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.marketplace.service.AnnuncioService;


@Controller
public class AdminController {
    @Autowired
    AnnuncioService annuncioService;

    @Autowired
    AnnuncioService commentoService;

    @PostMapping("/admin/annunci/{id}/delete")
    public String deleteAnnuncio(@PathVariable Long id) {       
        annuncioService.deleteById(id);
        return "redirect:/admin/annunci";
    }

    @PostMapping("/admin/commento/{commentoId}/annuncio/{annuncioId}/delete")
    public String deleteCommento(
        @PathVariable Long annuncioId,
        @PathVariable Long commentoId
        ) {       
        commentoService.deleteById(commentoId);
        return "redirect:/admin/annunci/" + annuncioId;
    }
}