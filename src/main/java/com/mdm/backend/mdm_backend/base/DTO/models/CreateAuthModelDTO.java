package com.mdm.backend.mdm_backend.base.DTO.models;

import lombok.Data;

import java.util.Set;
@Data
public class CreateAuthModelDTO {
    private String name;
    private String description;
    private Set<Long> access;
}
