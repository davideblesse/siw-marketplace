package it.uniroma3.marketplace.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.marketplace.costanti.Categoria;
import it.uniroma3.marketplace.model.Annuncio;
import it.uniroma3.marketplace.model.User;

public interface AnnuncioRepository extends JpaRepository<Annuncio, Long>{
    List<Annuncio> findByOwner(User owner);
    List<Annuncio> findByCategoria(Categoria categoria);
    List<Annuncio> findByCategoriaAndTitleContainingIgnoreCase(Categoria categoria, String name);
    List<Annuncio> findByTitleContainingIgnoreCase(String name);
    
}

