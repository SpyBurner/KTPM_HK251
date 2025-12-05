package com.its.iam.configuration;

import com.its.iam.entity.Role;
import com.its.iam.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("TEACHER"); 
        createRoleIfNotExists("TA");
        createRoleIfNotExists("STUDENT");
        
        log.info("Default roles initialized successfully");
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            Role savedRole = roleRepository.save(role);
            log.info("Created role: {}", roleName);
        } else {
            log.debug("Role already exists: {}", roleName);
        }
    }
}