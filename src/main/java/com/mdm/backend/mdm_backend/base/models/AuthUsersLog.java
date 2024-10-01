package com.mdm.backend.mdm_backend.base.models;

import jakarta.persistence.*;
import java.util.Date;
/*
 * Author: Chuluunbor
 * Email: loopersoftdev@gmail.com
 * Company: Looper Soft LLC
 * Created: September 20, 2024
 * Version: 1.0
 * License: MIT License
 * Description: This class handles menu authentication in the application
 */

@Entity
@Table(name = "auth_users_log")
public class AuthUsersLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private Date actionDate;

    // Many-to-one relationship with AuthUsers
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUsers authUser;

    // Getters and setters
    // Constructors, etc.
}
