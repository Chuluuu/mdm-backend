package com.mdm.backend.mdm_backend.base.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mdm.backend.mdm_backend.base.config.Many2ManyFieldSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
/*
 * Author: Chuluunbor
 * Email: loopersoftdev@gmail.com
 * Company: Looper Soft LLC
 * Created: September 24, 2024
 * Version: 1.0
 * License: MIT License
 * Description: This class handles groups authentication in the application
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_groups")
public class AuthGroups extends BaseEntity{

    @Column(name="name",unique = false,nullable = false)
    private String name;
    @Column(name="group_name",unique = false,nullable = true)
    private String groupName;
    @Column(name="description",nullable = false)
    private String description;
    @Column(name="not_delete_able")
    private Boolean notDeleteAble=false;

    @Column(name="active")
    private Boolean active=true;
    @ManyToMany
    @JoinTable(
            name = "auth_group_users", // Name of the join table
            joinColumns = @JoinColumn(name = "group_id"), // Foreign key from AuthGroups
            inverseJoinColumns = @JoinColumn(name = "user_id") // Foreign key from AuthUsers
    )
    @JsonSerialize(using = Many2ManyFieldSerializer.class)
    private Set<AuthUsers> authUsers;
}
