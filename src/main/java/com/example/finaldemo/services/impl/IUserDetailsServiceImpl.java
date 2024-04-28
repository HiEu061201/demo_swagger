package com.example.finaldemo.services.impl;

import com.example.finaldemo.dto.authDTO.LoginDTO;
import com.example.finaldemo.dto.authDTO.AuthenticationResponseDTO;
import com.example.finaldemo.entities.User;
import com.example.finaldemo.repository.UserRepository;
import com.example.finaldemo.services.interfaces.IUserDetailsService;
import com.example.finaldemo.utils.StringDealer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserDetailsServiceImpl implements IUserDetailsService {
    private final StringDealer stringDealer = new StringDealer();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final  AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;



        @Override
        public ResponseEntity<AuthenticationResponseDTO> authenticateUser(LoginDTO loginDTO) {
            //Trim inputs
            String email = stringDealer.trimMax(loginDTO.getEmail());
            String password = stringDealer.trimMax(loginDTO.getPassword());

            // Email is empty
            if ("".equals(email) || email == null) {
                return new ResponseEntity<>(new AuthenticationResponseDTO("Email không được để trống"), HttpStatus.BAD_REQUEST);
            }
            // Email is not valid
            if (!stringDealer.checkEmailRegex(email)) {
                return new ResponseEntity<>(new AuthenticationResponseDTO("Email không đúng định dạng"), HttpStatus.BAD_REQUEST);
            }
            // password is not valid
            if ("".equals(password) || password == null) {
                return new ResponseEntity<>(new AuthenticationResponseDTO("Password không được để trống"), HttpStatus.BAD_REQUEST);
            }

            Optional<User> userOpt = userRepository.findByEmailContainingIgnoreCase(email);
            System.out.println(userOpt);
            if (userOpt.isEmpty()) {
                return new ResponseEntity<>(new AuthenticationResponseDTO("Tài khoản hoặc mật khẩu sai"), HttpStatus.BAD_REQUEST);
            }

            //Check password
            if (!passwordEncoder.matches(password, userOpt.get().getPassword())) {
                return new ResponseEntity<>(new AuthenticationResponseDTO("Mật khẩu hoặc tài khoản sai"), HttpStatus.BAD_REQUEST);
            }

            //Authenticate the user
            try {
                Authentication auth = authenticationProvider.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password));
                SecurityContextHolder.getContext().setAuthentication(auth);
                User userOptional = userRepository.findByEmailContainingIgnoreCase(email).orElseThrow();
                AuthenticationResponseDTO response = generateTokenForUser(userOptional);
                return ResponseEntity.ok(response);
            } catch (AuthenticationException e) {
                return new ResponseEntity<>(new AuthenticationResponseDTO("Xác thực không thành công: " + e.getMessage()), HttpStatus.UNAUTHORIZED);
            }
        }

    private AuthenticationResponseDTO generateTokenForUser(User user) {
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDTO(jwtToken);
    }
}
