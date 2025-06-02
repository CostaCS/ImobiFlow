package com.example.crud.service;


import com.example.crud.domain.entitys.Cliente;
import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.ClienteRepository;
import com.example.crud.requests.RequestCliente;
import com.example.crud.services.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository repository;

    private Cliente cliente;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(UUID.randomUUID());

        cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNome("João");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua A");
        cliente.setUsuario(usuario);
    }

    @Test
    void testGetAllClientes() {
        List<Cliente> lista = List.of(cliente);
        when(repository.findAll()).thenReturn(lista);
        List<Cliente> resultado = clienteService.getAllClientes();
        assertEquals(1, resultado.size());
    }

    @Test
    void testRegisterCliente() {
        RequestCliente req = new RequestCliente(null, "João", "joao@email.com", "11999999999", "Rua A", null);
        clienteService.registerCliente(req);
        verify(repository).save(any(Cliente.class));
    }

    @Test
    void testUpdateCliente_Sucesso() {
        RequestCliente req = new RequestCliente(cliente.getId(), "Novo Nome", "novo@email.com", "11888888888", "Nova Rua", null);
        when(repository.findById(req.id())).thenReturn(Optional.of(cliente));
        Cliente atualizado = clienteService.updateCliente(req);
        assertEquals("Novo Nome", atualizado.getNome());
    }

    @Test
    void testUpdateCliente_NaoEncontrado() {
        UUID id = UUID.randomUUID();
        RequestCliente req = new RequestCliente(id, "Nome", "email@email.com", "11999999999", "Rua", null);
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clienteService.updateCliente(req));
    }

    @Test
    void testDeleteCliente_Sucesso() {
        when(repository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        clienteService.deleteCliente(cliente.getId());
        verify(repository).delete(cliente);
    }

    @Test
    void testDeleteCliente_NaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clienteService.deleteCliente(id));
    }

    @Test
    void testListarTodos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> page = new PageImpl<>(List.of(cliente), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);
        Page<Cliente> resultado = clienteService.listarTodos(pageable);
        assertEquals(1, resultado.getContent().size());
    }

    @Test
    void testSalvar_SemDuplicidade() {
        when(repository.findByEmailIgnoreCase(cliente.getEmail())).thenReturn(Optional.empty());
        clienteService.salvar(cliente, usuario);
        verify(repository).save(cliente);
    }

    @Test
    void testSalvar_ComDuplicidade() {
        Cliente existente = new Cliente();
        existente.setId(UUID.randomUUID());
        when(repository.findByEmailIgnoreCase(cliente.getEmail())).thenReturn(Optional.of(existente));
        assertThrows(IllegalArgumentException.class, () -> clienteService.salvar(cliente, usuario));
    }

    @Test
    void testDeletarDireto() {
        clienteService.deletar(cliente.getId());
        verify(repository).deleteById(cliente.getId());
    }

    @Test
    void testListarTodosPorUsuarioPaginado() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> page = new PageImpl<>(List.of(cliente));
        when(repository.findByUsuario(usuario, pageable)).thenReturn(page);
        Page<Cliente> resultado = clienteService.listarTodosPorUsuario(usuario, pageable);
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void testListarTodosPorUsuarioSemPaginacao() {
        List<Cliente> lista = List.of(cliente);
        when(repository.findByUsuario(usuario)).thenReturn(lista);
        List<Cliente> resultado = clienteService.listarTodosPorUsuario(usuario);
        assertEquals(1, resultado.size());
    }

    @Test
    void testContarTodos() {
        when(repository.countByUsuario(usuario)).thenReturn(10L);
        assertEquals(10L, clienteService.contarTodos(usuario));
    }

    @Test
    void testContarSemImobiliaria() {
        when(repository.countByUsuarioAndImobiliariaIsNull(usuario)).thenReturn(3L);
        assertEquals(3L, clienteService.contarSemImobiliaria(usuario));
    }

    @Test
    void testBuscarImobiliariaComMaisClientes() {
        when(repository.buscarNomeImobiliariaComMaisClientes(usuario)).thenReturn(List.of("ImobTop"));
        assertEquals("ImobTop", clienteService.buscarImobiliariaComMaisClientes(usuario));
    }

    @Test
    void testBuscarImobiliariaComMaisClientes_Vazio() {
        when(repository.buscarNomeImobiliariaComMaisClientes(usuario)).thenReturn(Collections.emptyList());
        assertNull(clienteService.buscarImobiliariaComMaisClientes(usuario));
    }
}

