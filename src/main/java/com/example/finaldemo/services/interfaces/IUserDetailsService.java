package com.example.finaldemo.services.interfaces;

import com.example.finaldemo.dto.authDTO.AuthenticationResponseDTO;
import com.example.finaldemo.dto.authDTO.LoginDTO;
import org.springframework.http.ResponseEntity;

public interface IUserDetailsService {
    ResponseEntity<AuthenticationResponseDTO>authenticateUser(LoginDTO loginDTO);
}
