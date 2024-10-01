package com.mdm.backend.mdm_backend.base.DTO.users;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String username;
    private String password;
    private String email;
}
