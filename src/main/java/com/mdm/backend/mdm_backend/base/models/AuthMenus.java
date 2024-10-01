package com.mdm.backend.mdm_backend.base.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mdm.backend.mdm_backend.base.config.Many2ManyFieldSerializer;
import com.mdm.backend.mdm_backend.base.exceptions.GlobalExceptionHandler;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
/*
 * Author: Chuluunbor
 * Email: loopersoftdev@gmail.com
 * Company: Looper Soft LLC
 * Created: September 25, 2024
 * Version: 1.0
 * License: MIT License
 * Description: This class handles menu authentication in the application
 */

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_menus")
public class AuthMenus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name",unique = false, nullable = false)
    private String name;
    @Column(name="menu_name",unique = true, nullable = false)
    private String menuName;
    @Column(name = "sequence")
    private Integer sequence=0;

    @Column(name="description",nullable = false)
    private String description;
    @Column(name="not_delete_able")
    private Boolean notDeleteAble=false;

    @ManyToOne
    @JoinColumn(name = "create_uid", nullable = true, updatable = false)
    private AuthUsers createUid;

    @Column(name = "create_date", updatable = false, nullable = false)
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "write_uid", nullable = true)
    private AuthUsers writeUid;

    @Column(name = "write_date")
    private LocalDateTime writeDate;

    @ManyToMany
    @JoinTable(
            name = "auth_menu_groups", // Name of the join table
            joinColumns = @JoinColumn(name = "menu_id"), // Foreign key from AuthMenu
            inverseJoinColumns = @JoinColumn(name = "group_id") // Foreign key from AuthGroups
    )
    @JsonSerialize(using = Many2ManyFieldSerializer.class)
    private Set<AuthGroups> authGroups;

    @ManyToOne
    @JoinColumn(name = "parent_menu_id")  // Foreign key to the parent menu
    @JsonBackReference
    private AuthMenus parentMenuId;


    @OneToMany(mappedBy = "parentMenuId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AuthMenus> childIds;

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
    public void addChildMenu(AuthMenus childMenu) {
        if (childMenu == null) {
            throw new IllegalArgumentException("Child menu cannot be null.");
        }
        if (this.id != null && this.id.equals(childMenu.getId())) {
            throw new IllegalArgumentException("A menu cannot be its own child.");
        }
        if (!childIds.contains(childMenu)) {
            childMenu.setParentMenuId(this);
            this.childIds.add(childMenu);
        }}
    public void removeChildMenu(AuthMenus childMenu) {
        this.childIds.remove(childMenu);
    }
}
