package com.example.crud.service;

import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.UsuarioRepository;
import com.example.crud.requests.RequestUsuario;
import com.example.crud.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private BCryptPasswordEncoder encoder;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("123456");
        usuario.setTelefone("11999999999");
        usuario.setData_cadastro(LocalDate.now());
    }

    @Test
    void testGetAllUsuarios() {
        when(repository.findAll()).thenReturn(List.of(usuario));
        List<Usuario> resultado = usuarioService.getAllUsuarios();
        assertEquals(1, resultado.size());
    }

    @Test
    void testRegisterUsuario() {
        RequestUsuario req = new RequestUsuario(null, "João", "joao@email.com", "123456", "11999999999", LocalDate.now());
        when(encoder.encode(req.senha())).thenReturn("senha_codificada");

        usuarioService.registerUsuario(req);

        verify(repository).save(any(Usuario.class));
    }

    @Test
    void testUpdateUsuario_Sucesso() {
        RequestUsuario req = new RequestUsuario(usuario.getId(), "Novo Nome", "novo@email.com", "novaSenha", "11988888888", LocalDate.now());
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(encoder.encode("novaSenha")).thenReturn("senha_hash");

        Usuario atualizado = usuarioService.updateUsuario(req);

        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals("senha_hash", atualizado.getSenha());
    }

    @Test
    void testUpdateUsuario_Falha() {
        UUID id = UUID.randomUUID();
        RequestUsuario req = new RequestUsuario(id, "Nome", "email", "senha", "telefone", LocalDate.now());

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.updateUsuario(req));
    }

    @Test
    void testDeleteUsuario_Sucesso() {
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        usuarioService.deleteUsuario(usuario.getId());
        verify(repository).delete(usuario);
    }

    @Test
    void testDeleteUsuario_Falha() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> usuarioService.deleteUsuario(id));
    }

    @Test
    void testSalvar() {
        usuarioService.salvar(usuario);
        verify(repository).save(usuario);
    }

    @Test
    void testBuscarPorEmail_Encontrado() {
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.buscarPorEmail(usuario.getEmail());
        assertEquals(usuario, resultado);
    }

    @Test
    void testBuscarPorEmail_NaoEncontrado() {
        when(repository.findByEmail("nao@existe.com")).thenReturn(Optional.empty());
        assertNull(usuarioService.buscarPorEmail("nao@existe.com"));
    }

    @Test
    void testFindById_Sucesso() {
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.findById(usuario.getId());
        assertEquals(usuario, resultado);
    }

    @Test
    void testFindById_Falha() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> usuarioService.findById(id));
    }

    @Test
    void testBuscarUsuarioPorToken() {
        when(repository.findByTokenRecuperacaoSenha("abc123")).thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.buscarUsuarioPorToken("abc123");
        assertEquals(usuario, resultado);
    }

    @Test
    void testBuscarUsuarioPorToken_NaoEncontrado() {
        when(repository.findByTokenRecuperacaoSenha("xyz")).thenReturn(Optional.empty());
        Usuario resultado = usuarioService.buscarUsuarioPorToken("xyz");
        assertNull(resultado);
    }

    @Test
    void testEmailJaCadastrado_Verdadeiro() {
        when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        assertTrue(usuarioService.emailJaCadastrado(usuario.getEmail()));
    }

    @Test
    void testEmailJaCadastrado_Falso() {
        when(repository.findByEmail("nao@existe.com")).thenReturn(Optional.empty());
        assertFalse(usuarioService.emailJaCadastrado("nao@existe.com"));
    }
}
