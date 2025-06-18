package com.example.crud.service;

import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.UsuarioRepository;
import com.example.crud.services.UsuarioDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceTest {

    @InjectMocks
    private UsuarioDetailsService usuarioDetailsService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("senha123");
    }

    @Test
    void testLoadUserByUsername_Sucesso() {
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario));

        UserDetails resultado = usuarioDetailsService.loadUserByUsername("joao@email.com");

        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getUsername());
        assertEquals("senha123", resultado.getPassword());
    }

    @Test
    void testLoadUserByUsername_UsuarioNaoEncontrado() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                usuarioDetailsService.loadUserByUsername("naoexiste@email.com"));
    }
}
