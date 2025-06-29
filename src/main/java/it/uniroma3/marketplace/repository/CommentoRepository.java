// src/main/java/it/uniroma3/marketplace/repository/CommentoRepository.java
package it.uniroma3.marketplace.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.marketplace.model.Commento;
import it.uniroma3.marketplace.model.Annuncio;

public interface CommentoRepository extends JpaRepository<Commento,Long> {
    List<Commento> findByAnnuncioOrderByOfferDesc(Annuncio annuncio);
}
