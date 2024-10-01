package com.mdm.backend.mdm_backend.base.controller;
import com.mdm.backend.mdm_backend.base.DTO.users.LoginRequestDTO;
import com.mdm.backend.mdm_backend.base.config.JwtTokenUtil;
import com.mdm.backend.mdm_backend.base.config.WebSecurityConfig;
import com.mdm.backend.mdm_backend.base.models.AuthUsers;
import com.mdm.backend.mdm_backend.base.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/web")
public class AuthController {

    @Autowired
    private final AuthService authService;
    @Autowired
    private final WebSecurityConfig webSecurityConfig;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    public AuthController(AuthService authService, WebSecurityConfig webSecurityConfig) {
        this.authService = authService;
        this.webSecurityConfig = webSecurityConfig;
    }
    @PostMapping({"/authenticate", "/authenticate/"})
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody LoginRequestDTO user) {
        AuthUsers loginUser = authService.loadUserByUsername(user.getUsername());
        if (loginUser != null && webSecurityConfig.passwordEncoder().matches(user.getPassword(), loginUser.getPassword())) {
            // Update the loginDate
            authService.updateLoginDate(loginUser);
            // Generate JWT Access and Refresh tokens
            String accessToken = jwtTokenUtil.generateAccessToken(loginUser.getUsername());
            String refreshToken = jwtTokenUtil.generateRefreshToken(loginUser.getUsername());

            // Create a response map
            Map<String, Object> response = new HashMap<>();
            response.put("user", loginUser);
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);

            // Return tokens
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Collections.singletonMap("error", "Username or Password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
