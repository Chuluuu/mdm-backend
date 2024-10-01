package com.mdm.backend.mdm_backend.base.config;

import com.mdm.backend.mdm_backend.base.exceptions.GlobalExceptionHandler;
import com.mdm.backend.mdm_backend.base.models.AuthUsers;
import com.mdm.backend.mdm_backend.base.repository.AuthUsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DefaultAdminCreation {
    @Autowired
    private AuthUsersRepository authUsersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${adminUserCreation.username}")
    private String adminUsername;

    @Value("${adminUserCreation.email}")
    private String adminEmail;

    @Value("${adminUserCreation.password}")
    private String adminPassword;
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Bean
    public CommandLineRunner initializeDefaultAdminUser() {
        return args -> {
            Optional<AuthUsers> optionalAdminUser = Optional.ofNullable(authUsersRepository.findByUsername(adminUsername));
            if (optionalAdminUser.isEmpty()) {
                AuthUsers adminUser = new AuthUsers();
                adminUser.setUsername(adminUsername);
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(passwordEncoder.encode(adminPassword));
                authUsersRepository.save(adminUser);
                logger.info("Default admin user created");
            }
        };
    }
}
