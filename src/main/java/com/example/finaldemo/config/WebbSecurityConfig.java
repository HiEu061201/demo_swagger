
package com.example.finaldemo.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebbSecurityConfig {

  private final AuthenticationProvider authProvider;
  private final JwtAuthenticationFilter jwtAuthFilter;




    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowCredentials(true);
            configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
            configuration.setAllowedOrigins(Collections.singletonList("*"));

            return configuration;
        }).and().csrf().disable();

        final String[] NO_ROLE = {
                "/graphiql/**",
                "/api/v1/auth/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/webjars/**"

        };
        final String[] ADMIN = {
                "/api/v1/admin/**"
        };
        final String[] CUS = {
                "/api/v1/cus/**"
        };
        http
                .authorizeHttpRequests()
                .requestMatchers(NO_ROLE).permitAll()
                .and().authorizeHttpRequests().requestMatchers(ADMIN)
                .hasAuthority("ADMIN").and().authorizeHttpRequests().requestMatchers(CUS).hasAuthority("CUSTOMER")
                .and().authorizeHttpRequests().anyRequest().authenticated()
                .and().sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
