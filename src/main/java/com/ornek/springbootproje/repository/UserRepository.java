package com.ornek.springbootproje.repository;

import com.ornek.springbootproje.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);
    void deleteById(Long id);
    Optional<User> findById(Long id);
    List<User> findAll();
    @Query(name = "User.findByEmail")
    Optional<User> findByEmail(String email);
}