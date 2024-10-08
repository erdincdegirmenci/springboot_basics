package com.ornek.springbootproje.service;

import com.ornek.springbootproje.entities.Role;
import com.ornek.springbootproje.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

//    public List<Role> findByUserId(Long userId) {
//        return roleRepository.findByUserId(userId);
//    }
//
//    public List<Role> findAll() {
//        return roleRepository.findAll();
//    }
//
//    public List<Role> findRolesWithPermissions() {
//        return roleRepository.findRolesWithPermissions();
//    }

//    public List<Role> getPermissionsByRoleId(Long roleId) {
//        return roleRepository.findPermissionsByRoleId(roleId);
//    }
}
