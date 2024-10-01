package com.mdm.backend.mdm_backend.base.DTO.menus;

import lombok.Data;

@Data
public class CreateMenuDTO {
    private String name;
    private String description;
    private String modelName;
    private Boolean active;
}
