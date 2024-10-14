package com.ornek.springbootproje.repository;

import com.ornek.springbootproje.entities.User;
import com.ornek.springbootproje.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole save(UserRole userRole);
    Optional<UserRole> findByUserid(Long userid);
}