package com.ornek.springbootproje.repository;

import com.ornek.springbootproje.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role save(Role role);
    List<Role> findAll();
}