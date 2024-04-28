package com.example.finaldemo.controller;

import com.example.finaldemo.dto.authDTO.LoginDTO;
import com.example.finaldemo.dto.authDTO.AuthenticationResponseDTO;
import com.example.finaldemo.services.interfaces.IUserDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final IUserDetailsService authenticateUser;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
        return authenticateUser.authenticateUser(loginDTO);
    }




}
