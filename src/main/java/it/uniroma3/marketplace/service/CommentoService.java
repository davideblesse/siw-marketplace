// src/main/java/it/uniroma3/marketplace/service/CommentoService.java
package it.uniroma3.marketplace.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.marketplace.model.Commento;
import it.uniroma3.marketplace.model.Annuncio;
import it.uniroma3.marketplace.repository.CommentoRepository;

@Service
public class CommentoService {
    @Autowired
    private CommentoRepository repo;

    public Commento save(Commento c) {
        return this.repo.save(c);
    }
    public void deleteById(Long id) {
        this.repo.deleteById(id);
    }
    public Commento findById(Long id) {
        return this.repo.findById(id).orElse(null);
    }
    public List<Commento> findByAnnuncioDesc(Annuncio a) {
        return this.repo.findByAnnuncioOrderByOfferDesc(a);
    }
}
