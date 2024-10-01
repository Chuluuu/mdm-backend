package com.mdm.backend.mdm_backend.base.DTO.users;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String email;
    private String password;
}
