package it.uniroma3.marketplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.marketplace.costanti.Categoria;
import it.uniroma3.marketplace.model.Annuncio;
import it.uniroma3.marketplace.model.User;
import it.uniroma3.marketplace.repository.AnnuncioRepository;

@Service
public class AnnuncioService {
    
    @Autowired
    private AnnuncioRepository annuncioRepo;
    
    public List<Annuncio> findAll() {
        return (List<Annuncio>) this.annuncioRepo.findAll();
    }

    public List<Annuncio> findByCategoria(Categoria categoria){
        return (List<Annuncio>) annuncioRepo.findByCategoria(categoria);
    }

    public List<Annuncio> findByCategoriaAndNome(Categoria categoria, String nome){
        return annuncioRepo.findByCategoriaAndTitleContainingIgnoreCase(categoria, nome);
    }

    public Annuncio findById(Long id){
        return this.annuncioRepo.findById(id).orElse(null);
    }
    
    public List<Annuncio> findByOwner(User user){
        return annuncioRepo.findByOwner(user);
    }


    public void save(Annuncio annuncio){
        this.annuncioRepo.save(annuncio);
    }

    public void delete(Annuncio annuncio) {
        this.annuncioRepo.delete(annuncio);
    }

    public void deleteById(Long id) {
        this.annuncioRepo.deleteById(id);
    }
}