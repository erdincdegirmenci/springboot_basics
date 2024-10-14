package com.ornek.springbootproje.service;

import com.ornek.springbootproje.entities.Role;
import com.ornek.springbootproje.entities.UserRole;
import com.ornek.springbootproje.repository.RoleRepository;
import com.ornek.springbootproje.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository,UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }
    public Role AddRole(Role role) {
        return roleRepository.save(role);
    }

    public Optional<UserRole> findByUserId(Long userId) {
        return userRoleRepository.findByUserid(userId);
    }

    public List<Role> GetAllRoles() {
        return roleRepository.findAll();
    }
}
