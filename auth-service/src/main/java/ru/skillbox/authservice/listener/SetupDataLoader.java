package ru.skillbox.authservice.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.authservice.domain.Role;
import ru.skillbox.authservice.domain.User;
import ru.skillbox.authservice.repository.RoleRepository;
import ru.skillbox.authservice.repository.UserRepository;

import java.util.Collections;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        if (userRepository.findByName("admin").isPresent()) return;
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setName("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRoles(Collections.singletonList(adminRole));
        userRepository.save(user);
    }

    @Transactional
    void createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            roleRepository.save(new Role(name));
        }
    }
}
