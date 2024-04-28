package com.example.finaldemo.dto.authDTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class AuthenticationResponseDTO {
    private String token;
}
