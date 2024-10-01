package com.mdm.backend.mdm_backend.base.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
/*
 * Author: Chuluunbor
 * Email: loopersoftdev@gmail.com
 * Company: Looper Soft LLC
 * Created: September 24, 2024
 * Version: 1.0
 * License: MIT License
 * Description: This class handles model access authentication in the application
 */

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_models_access")
 public class AuthModelsAccess {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name="name",nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "group_id",nullable = true,updatable = true)
    AuthGroups groupId;
    @ManyToOne
    @JsonBackReference // This side is the backward reference
    @JoinColumn(name = "model_id")
    AuthModels authModelId;
    @Column(name="perm_read")
    private Boolean permRead=false;
    @Column(name="perm_create")
    private Boolean permCreate=false;
    @Column(name="perm_update")
    private Boolean permUpdate=false;
    @Column(name="perm_delete")
    private Boolean permDelete=false;


}
