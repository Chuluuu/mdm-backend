package com.mdm.backend.mdm_backend.base.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mdm.backend.mdm_backend.base.config.Many2OneFieldSerializer;
import com.mdm.backend.mdm_backend.base.exceptions.GlobalExceptionHandler;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
/*
 * Author: Chuluunbor
 * Email: loopersoftdev@gmail.com
 * Company: Looper Soft LLC
 * Created: September 20, 2024
 * Version: 1.0
 * License: MIT License
 * Description: This class handles menu authentication in the application
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


//    @ManyToOne
//    @JoinColumn(name = "company_id", nullable = false)
//    @JsonManagedReference
//    private AuthCompany companyId;

    @ManyToOne
    @JoinColumn(name = "create_uid", nullable = true, updatable = false)
    @JsonManagedReference
    @JsonSerialize(using = Many2OneFieldSerializer.class)
    private AuthUsers createUid;

    @Column(name = "create_date", updatable = false, nullable = false)
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "write_uid", nullable = true)
    @JsonSerialize(using = Many2OneFieldSerializer.class)
    private AuthUsers writeUid;

    @Column(name = "write_date")
    private LocalDateTime writeDate;

    private String name;
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

    public Long getId() {
        return id;
    }
    public String getDisplayName(){
        return name ;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(LocalDateTime writeDate) {
        this.writeDate = writeDate;
    }

    public AuthUsers getCreateUid() {
        return createUid;
    }

    public void setCreateUid() {
        try {
            AuthUsers loggedInUser = (AuthUsers) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (loggedInUser != null) {
                this.createUid = loggedInUser;
            }
        } catch (ClassCastException | NullPointerException ex) {
            // Handle the exception (e.g., log or set default value)
            logger.error("Error occurred: {}", ex.getMessage(), ex);

        }
    }

    public AuthUsers getWriteUid() {
        return writeUid;
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

