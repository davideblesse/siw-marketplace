package it.uniroma3.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.marketplace.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
