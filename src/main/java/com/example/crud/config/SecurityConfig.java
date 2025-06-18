package com.example.crud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthSuccessHandler authSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/css/**", "/js/**", "/images/**",
                                "/usuario/cadastro",
                                "/usuario/recuperar-senha",
                                "/recuperar-senha",
                                "/usuario/recuperar-senha/**",
                                "/usuario/resetar-senha",
                                "/teste",
                                "/usuario/resetar-senha/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/recuperar-senha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/resetar-senha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/teste").permitAll()
                        .requestMatchers("/usuario/perfil").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .successHandler(authSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
