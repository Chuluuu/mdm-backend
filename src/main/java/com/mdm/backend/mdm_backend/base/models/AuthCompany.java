package com.mdm.backend.mdm_backend.base.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

/*
 * Author: Chuluunbor
 * Email: loopersoftdev@gmail.com
 * Company: Looper Soft LLC
 * Created: September 25, 2024
 * Version: 1.0
 * License: MIT License
 * Description: This class handles company authentication in the application
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_company")
public class AuthCompany extends BaseEntity{


    @Column(name="name",nullable = false)
    private String name;
    @Column(name="description",nullable = false)
    private String description;

    @Column(name="code",unique = true,nullable = false)
    private String code;
}
