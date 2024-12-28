package ru.skillbox.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.authservice.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
