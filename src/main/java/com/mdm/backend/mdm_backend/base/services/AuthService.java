package com.mdm.backend.mdm_backend.base.services;

import com.mdm.backend.mdm_backend.base.DTO.users.CreateUserDTO;
import com.mdm.backend.mdm_backend.base.DTO.users.UserUpdateDTO;
import com.mdm.backend.mdm_backend.base.models.AuthGroups;
import com.mdm.backend.mdm_backend.base.models.AuthUsers;
import com.mdm.backend.mdm_backend.base.repository.AuthUsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Order(1)
public class AuthService implements UserDetailsService {


    private final AuthUsersRepository authUsersRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthUsersRepository authUsersRepository, PasswordEncoder passwordEncoder) {
        this.authUsersRepository = authUsersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public AuthUsers loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieves user details by username from the database
        return authUsersRepository.findByUsername(username);
    }

    public List<AuthUsers> getAllUsers() {
        return authUsersRepository.findAll();
    }

    @Transactional
    public AuthUsers create(CreateUserDTO userRequest) {
        // Validate input
        if (userRequest.getUsername() == null || userRequest.getPassword() == null) {
            throw new IllegalArgumentException("Username and password are required.");
        }
        if (userRequest.getEmail() == null ) {
            throw new IllegalArgumentException("Email is required.");
        }
        AuthUsers foundUser = authUsersRepository.findByUsername(userRequest.getUsername());
        if (foundUser !=null ) {
            throw new IllegalArgumentException("This username already exists.");
        }
        AuthUsers foundUserWithEmail = authUsersRepository.findByEmail(userRequest.getEmail());
        if (foundUserWithEmail !=null ) {
            throw new IllegalArgumentException("This Email already exists.");
        }
        AuthUsers newUser = new AuthUsers();
        newUser.setUsername(userRequest.getUsername());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword())); // Encode password

        // Save the user and generate ID only on success
        return authUsersRepository.save(newUser);
    }
    @Transactional
    public Optional<AuthUsers> update(Long id, UserUpdateDTO updatedUserRequest) {
        return authUsersRepository.findById(id).map(user -> {
            user.setEmail(updatedUserRequest.getEmail());
            if (updatedUserRequest.getUsername() != null) {
                user.setUsername(updatedUserRequest.getUsername());
            }
            if (updatedUserRequest.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updatedUserRequest.getPassword())); // Re-encode password
            }
            return authUsersRepository.save(user);
        });
    }
    public Optional<AuthUsers> delete(Long id) {
        Optional<AuthUsers> user = authUsersRepository.findById(id);
        user.ifPresent(authUsersRepository::delete);
        return user;
    }
    @Transactional
    public void updateLoginDate(AuthUsers user) {
        user.setLoginDate(LocalDateTime.now());
        authUsersRepository.save(user); // Save the updated user
    }
    public Optional<AuthUsers>  getUserById(Long id) {
        return authUsersRepository.findById(id);
    }
}
