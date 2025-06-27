package it.uniroma3.marketplace.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.marketplace.model.Credentials;

public interface CredentialsRepository extends JpaRepository<Credentials, Long>{
    public Optional<Credentials> findByUsername (String username);
    public boolean existsByUsername (String username);
}
