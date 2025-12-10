package com.its.iam.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.iam.entity.Role;
import com.its.iam.entity.User;
import com.its.iam.repository.RoleRepository;
import com.its.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsersFromJson();
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
            log.info("Created role: {} (id={})", roleName, savedRole.getId());
        } else {
            log.debug("Role already exists: {}", roleName);
        }
    }

    private void initializeUsersFromJson() {
        try (InputStream is = new ClassPathResource("mock-data/iam-users.json").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            // mapping from numeric roleId in JSON to role name - assumption
            Map<Integer, String> roleMap = Map.of(
                    1, "ADMIN",
                    2, "TEACHER",
                    3, "TA",
                    4, "STUDENT"
            );

            for (JsonNode u : root.path("users")) {
                String username = u.path("username").asText(null);
                String password = u.path("password").asText(null);
                String email = u.path("email").asText(null);
                String displayName = u.path("displayName").asText(null);
                int roleId = u.path("roleId").asInt(-1);

                if (username == null || email == null) continue;

                if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
                    log.debug("User already exists: {}", username);
                    continue;
                }

                String roleName = roleMap.getOrDefault(roleId, "STUDENT");
                Optional<Role> roleOpt = roleRepository.findByName(roleName);
                Role role = roleOpt.orElseGet(() -> roleRepository.findByName("STUDENT").orElse(null));

                User user = User.builder()
                        .username(username)
                        .email(email)
                        .displayName(displayName == null ? username : displayName)
                        .passwordHash(passwordEncoder.encode(password))
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .role(role)
                        .build();

                User saved = userRepository.save(user);
                log.info("Created user: {} (id={}) role={}", saved.getUsername(), saved.getId(), role != null ? role.getName() : "null");
            }
        } catch (Exception e) {
            log.warn("No iam-users.json found on classpath or failed to load users: {}", e.getMessage());
        }
    }
}