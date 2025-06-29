package it.uniroma3.marketplace.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.marketplace.costanti.Categoria;
import it.uniroma3.marketplace.model.Annuncio;
import it.uniroma3.marketplace.model.ImageEntity;
import it.uniroma3.marketplace.model.User;
import it.uniroma3.marketplace.service.AnnuncioService;
import it.uniroma3.marketplace.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AnnuncioController {

    @Autowired
    private AnnuncioService annuncioService;

    @Autowired
    private UserService userService;

    // --- public list / detail ---

    @GetMapping("/annunci")
    public String listAnnunci(
            @RequestParam(required=false) Categoria categoria,
            @RequestParam(required=false) String nome,
            Model model) {

        List<Annuncio> annunci;
        if (nome != null && !nome.isBlank()) {
            annunci = annuncioService.findByCategoriaAndNome(categoria, nome);
        } else if (categoria != null) {
            annunci = annuncioService.findByCategoria(categoria);
        } else {
            annunci = annuncioService.findAll();
        }

        model.addAttribute("annunci", annunci);
        model.addAttribute("categorie", Categoria.values());
        model.addAttribute("categoriaSelezionata", categoria);
        model.addAttribute("nomeSelezionato", nome);
        return "annunci";
    }

    @GetMapping("/annunci/{id}")
    public String showAnnuncio(@PathVariable Long id, Model model) {
        Annuncio annuncio = annuncioService.findById(id);
        if (annuncio == null) return "error";
        model.addAttribute("annuncio", annuncio);
        return "annuncio";
    }

    // --- user’s own annunci list & detail ---

    @GetMapping("/user/annunci")
    public String listUserAnnunci(
            @RequestParam(required=false) Categoria categoria,
            @RequestParam(required=false) String nome,
            Model model) {

        User current = userService.getCurrentUser();
        if (current == null) return "redirect:/login";

        List<Annuncio> annunci;
        if (nome != null && !nome.isBlank()) {
            annunci = annuncioService.findByCategoriaAndNome(categoria, nome);
        } else if (categoria != null) {
            annunci = annuncioService.findByCategoria(categoria);
        } else {
            annunci = annuncioService.findAll();
        }

        model.addAttribute("user", current);
        model.addAttribute("annunci", annunci);
        model.addAttribute("categorie", Categoria.values());
        model.addAttribute("categoriaSelezionata", categoria);
        model.addAttribute("nomeSelezionato", nome);
        return "user/annunci";
    }

    @GetMapping("/user/annunci/{id}")
    public String showUserAnnuncio(@PathVariable Long id, Model model) {
        User current = userService.getCurrentUser();
        if (current == null) return "redirect:/login";

        Annuncio annuncio = annuncioService.findById(id);
        if (annuncio == null) return "error";

        model.addAttribute("user", current);
        model.addAttribute("annuncio", annuncio);
        return "user/annuncio";
    }

    // --- delete ---

    @PostMapping("/user/annunci/{id}/delete")
    public String delete(@PathVariable Long id) {
        User current = userService.getCurrentUser();
        if (current == null) return "redirect:/login";

        annuncioService.deleteById(id);
        return "redirect:/user/" + current.getId();
    }

    // --- create form & handler ---

    @GetMapping("/user/annunci/new")
    public String newUserAnnuncioForm(Model model) {
        User current = userService.getCurrentUser();
        if (current == null) return "redirect:/login";

        model.addAttribute("user", current);
        model.addAttribute("annuncio", new Annuncio());
        model.addAttribute("allCats", Categoria.values());
        return "user/annuncio-form";
    }

    @PostMapping("/annunci")
    public String createAnnuncio(
            @ModelAttribute Annuncio annuncio,
            @RequestParam("imageFiles") MultipartFile[] imageFiles
    ) throws IOException {
        User current = userService.getCurrentUser();
        if (current == null) return "redirect:/login";

        // set the owner
        annuncio.setOwner(current);

        // process images
        List<ImageEntity> images = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                Path target = Paths
                    .get("src/main/resources/static/images/")
                    .resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(),
                           target,
                           StandardCopyOption.REPLACE_EXISTING);
                images.add(new ImageEntity(file.getOriginalFilename()));
            }
        }
        annuncio.setImages(images);

        annuncioService.save(annuncio);
        return "redirect:/annunci/" + annuncio.getId();
    }

    // --- edit form & handler ---

    @GetMapping("/user/annunci/{id}/edit")
    public String editUserAnnuncioForm(@PathVariable Long id, Model model) {
        User current = userService.getCurrentUser();
        if (current == null) return "redirect:/login";

        Annuncio existing = annuncioService.findById(id);
        if (existing == null) return "error";

        model.addAttribute("user", current);
        model.addAttribute("annuncio", existing);
        model.addAttribute("allCats", Categoria.values());
        return "user/annuncio-form";
    }

    @PostMapping("/annunci/{id}")
    public String updateAnnuncio(
            @PathVariable Long id,
            @ModelAttribute Annuncio annuncio,
            @RequestParam("imageFiles") MultipartFile[] imageFiles
    ) throws IOException {
        User current = userService.getCurrentUser();
        if (current == null) return "redirect:/login";

        Annuncio existing = annuncioService.findById(id);
        if (existing == null) return "error";

        // copy editable fields
        existing.setTitle(annuncio.getTitle());
        existing.setPrice(annuncio.getPrice());
        existing.setCategoria(annuncio.getCategoria());

        // replace images
        List<ImageEntity> images = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                Path target = Paths
                    .get("src/main/resources/static/images/")
                    .resolve(file.getOriginalFilename());
                Files.copy(file.getInputStream(),
                           target,
                           StandardCopyOption.REPLACE_EXISTING);
                images.add(new ImageEntity(file.getOriginalFilename()));
            }
        }
        existing.setImages(images);

        annuncioService.save(existing);
        return "redirect:/annunci/" + existing.getId();
    }
}
ice.delete(a);                       // aggiungi metodo delete nel service
            return "redirect:/user/annunci";
        }
}
