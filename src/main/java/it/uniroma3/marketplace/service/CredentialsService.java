package it.uniroma3.marketplace.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.marketplace.model.Credentials;
import static it.uniroma3.marketplace.model.Credentials.DEFAULT_ROLE;
import it.uniroma3.marketplace.model.User;
import it.uniroma3.marketplace.repository.CredentialsRepository;
import jakarta.validation.Valid;

@Service
public class CredentialsService {
    @Autowired
    private CredentialsRepository credentialsRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Credentials findByUsername(String username){
        return this.credentialsRepo.findByUsername(username).orElse(null);
    }

    public boolean existsByUsername(String username){
        return this.credentialsRepo.existsByUsername(username);
    }

    public void saveCredentials(@Valid Credentials credentials){
        credentials.setRole(DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        this.credentialsRepo.save(credentials);
    }

    public User getCurrentUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.findByUsername(userDetails.getUsername()).getUser();
    }
    
}
