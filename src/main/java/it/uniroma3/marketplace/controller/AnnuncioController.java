package it.uniroma3.marketplace.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/annunci")
    public String listAnnunci(
            @RequestParam(required=false) Categoria categoria,
            @RequestParam(required=false) String nome,
            Model model) {

        List<Annuncio> annunci;
        if (nome != null && !nome.isBlank()) {
            annunci = annuncioService.findByCategoriaAndNome(categoria, nome);
        }
        else if (categoria != null) {
            annunci = annuncioService.findByCategoria(categoria);
        }
        else {
            annunci = annuncioService.findAll();
        }

        model.addAttribute("annunci", annunci);
        model.addAttribute("categorie", Categoria.values());
        model.addAttribute("categoriaSelezionata", categoria);
        model.addAttribute("nomeSelezionato", nome);
        return "annunci";
    }

    @GetMapping("/annunci/{id}")
    public String showAnnuncio(@PathVariable("id") Long id, Model model) {
        Annuncio annuncio = annuncioService.findById(id);
        if (annuncio == null)
            return "error";
        model.addAttribute("annuncio", annuncio);
        return "annuncio";
    }

    @GetMapping("/user/annunci")
    public String showMyAnnunci(Model model) {
        User me = userService.getCurrentUser();
        if (me == null) return "redirect:/login";

        model.addAttribute("annunci",
            annuncioService.findByOwner(me));  
        return "user/annunci";
    }

    @GetMapping("/user/annuncio-form")
    public String newAnnuncio(Model m) {
        m.addAttribute("annuncio", new Annuncio());
        m.addAttribute("categorie", Categoria.values());
        m.addAttribute("editMode", false);           // <── creazione
        return "user/annuncio-form";
    }

    @PostMapping("/user/annunci")
    public String createAnnuncio(
        @ModelAttribute("annuncio") @Valid Annuncio annuncio,
        BindingResult bindingResult,
        @RequestParam("imageFiles") MultipartFile[] imageFiles, 
        Model model) throws IOException {

    if (bindingResult.hasErrors()) {
        model.addAttribute("categorie", Categoria.values());
        return "user/annuncio-form";
    }

    // 1️⃣ Salvo fisicamente ogni file e creo l’ImageEntity corrispondente
    List<ImageEntity> immagini = new ArrayList<>();
    String uploadDir = "src/main/resources/static/images"; // cartella delle risorse statiche

    for (MultipartFile file : imageFiles) {
        if (file != null && !file.isEmpty()) {
        // genera un nome univoco
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        // salva fisicamente
        Path target = Paths.get(uploadDir).resolve(filename);
        Files.createDirectories(target.getParent());
        Files.write(target, file.getBytes());

        // crea l’entità
        ImageEntity img = new ImageEntity();
        img.setName(filename);  // la getName() restituirà “/images/ + filename”
        immagini.add(img);
        }
    }

    // 2️⃣ Aggancio le immagini all’annuncio e salvo via JPA
    annuncio.setImages(immagini);


    annuncio.setOwner(userService.getCurrentUser());

    annuncioService.save(annuncio);

    return "redirect:/user/annunci";
    }



    /* -------- form di modifica -------- */
    @GetMapping("/user/annunci/{id}/edit")
    public String editForm(@PathVariable Long id, Model m) {
        Annuncio a = annuncioService.findById(id);
        if (a == null || !a.getOwner().equals(userService.getCurrentUser()))
            return "redirect:/user/annunci";

        m.addAttribute("annuncio", a);
        m.addAttribute("categorie", Categoria.values());
        m.addAttribute("editMode", true);            // <── modifica
        return "user/annuncio-form";
    }

    /* -------- update -------- */
    @PostMapping("/user/annunci/{id}/edit")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute("annuncio") Annuncio annuncio,
                        BindingResult br, Model m){
        if (br.hasErrors()){
            m.addAttribute("categorie", Categoria.values());
            return "user/annuncio-form";
        }
        Annuncio db = annuncioService.findById(id);
        if (db==null || !db.getOwner().equals(userService.getCurrentUser()))
            return "redirect:/user/annunci";

        // copia campi editabili
        db.setTitle(annuncio.getTitle());
        db.setPrice(annuncio.getPrice());
        db.setCategoria(annuncio.getCategoria());
        /* (se vuoi: aggiornamento immagini) */

        annuncioService.save(db);
        return "redirect:/user/annunci";
    }

    /* -------- delete -------- */
    @PostMapping("/user/annunci/{id}/delete")
    public String delete(@PathVariable Long id){
        Annuncio a = annuncioService.findById(id);
        if (a!=null && a.getOwner().equals(userService.getCurrentUser()))
            annuncioService.delete(a);                       // aggiungi metodo delete nel service
        return "redirect:/user/annunci";
    }

}
