package com.mdm.backend.mdm_backend.base.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mdm.backend.mdm_backend.base.config.Many2OneFieldSerializer;
import com.mdm.backend.mdm_backend.base.exceptions.GlobalExceptionHandler;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
/*
 * Author: Chuluunbor
 * Email: loopersoftdev@gmail.com
 * Company: Looper Soft LLC
 * Created: September 24, 2024
 * Version: 1.0
 * License: MIT License
 * Description: This class handles model authentication in the application
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "auth_models")
public class AuthModels {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name="description",nullable = false)
    private String description;

    @Column(name="model_name",unique = true,nullable = false)
    private String modelName;
    @Column(name="active")
    private Boolean active=true;

    @OneToMany(mappedBy = "authModelId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AuthModelsAccess> accessIds;

    //@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @ManyToOne
    @JoinColumn(name = "create_uid", nullable = true, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Avoid lazy loading issues
    @JsonSerialize(using = Many2OneFieldSerializer.class)
    private AuthUsers createUid;

    @Column(name = "create_date", updatable = false, nullable = false)
    private LocalDateTime createDate;

    // @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @ManyToOne
    @JoinColumn(name = "write_uid", nullable = true)
    @JsonSerialize(using = Many2OneFieldSerializer.class)
    private AuthUsers writeUid;

    @Column(name = "write_date")
    private LocalDateTime writeDate;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @PrePersist
    protected void onCreate() {
        try {
            AuthUsers loggedInUser = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof AuthUsers) {
                loggedInUser = (AuthUsers) authentication.getPrincipal();
            }
            if (loggedInUser != null) {
                this.createUid = loggedInUser;
                this.writeUid = loggedInUser;
            }
        } catch (ClassCastException | NullPointerException ex) {
            // Handle the exception (e.g., log or set default value)
            logger.error("Error occurred: {}", ex.getMessage(), ex);
        }
        this.createDate = LocalDateTime.now();
        this.writeDate = LocalDateTime.now();
//        setCreateUid();  // Automatically set the createUid
//        setWriteUid();
    }

    @PreUpdate
    protected void onUpdate() {
        this.writeDate = LocalDateTime.now();
        setWriteUid();  // Automatically set the writeUid
    }
    public void setWriteUid() {
        try {
            AuthUsers loggedInUser = (AuthUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (loggedInUser != null) {
                this.writeUid = loggedInUser;
            }
        } catch (ClassCastException | NullPointerException ex) {
            // Handle the exception (e.g., log or set default value)
            logger.error("Error occurred: {}", ex.getMessage(), ex);
        }
    }
}

