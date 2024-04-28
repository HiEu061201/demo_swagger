package com.example.finaldemo.dto.userDTO;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String userEmail;
    private String password;
    private String rePassword;
    private String userRole;
}
