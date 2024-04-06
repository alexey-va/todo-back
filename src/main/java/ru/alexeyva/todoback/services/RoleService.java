package ru.alexeyva.todoback.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.alexeyva.todoback.exception.notfound.RoleNotFoundException;
import ru.alexeyva.todoback.model.Role;
import ru.alexeyva.todoback.repos.RoleRepo;

@Service
@RequiredArgsConstructor
class RoleService {

    final RoleRepo roleRepo;

    @Cacheable("roles")
    public Role userRole() {
        Role role = roleRepo.findByName("USER");
        if (role == null) throw new RoleNotFoundException("user");
        return role;
    }

    @Cacheable("roles")
    public Role adminRole() {
        Role role = roleRepo.findByName("ADMIN");
        if (role == null) throw new RoleNotFoundException("admin");
        return role;
    }

}