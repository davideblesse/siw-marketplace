package it.uniroma3.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.marketplace.model.User;
import it.uniroma3.marketplace.repository.UserRepository;
import jakarta.validation.Valid;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CredentialsService credentialsService;

    public User getCurrentUser(){
        return this.credentialsService.getCurrentUser();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser(@Valid User user){
        this.userRepository.save(user);
    }
}
