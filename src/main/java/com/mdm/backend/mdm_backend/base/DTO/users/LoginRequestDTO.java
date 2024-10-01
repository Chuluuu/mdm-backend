package com.mdm.backend.mdm_backend.base.DTO.users;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String username;
    private String password;
}
