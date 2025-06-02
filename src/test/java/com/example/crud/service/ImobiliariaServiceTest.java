package com.example.crud.service;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.ImobiliariaRepository;
import com.example.crud.services.ImobiliariaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImobiliariaServiceTest {

    @InjectMocks
    private ImobiliariaService imobiliariaService;

    @Mock
    private ImobiliariaRepository repository;

    private Usuario usuario;
    private Imobiliaria imobiliaria;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(UUID.randomUUID());

        imobiliaria = new Imobiliaria();
        imobiliaria.setId(UUID.randomUUID());
        imobiliaria.setUsuario(usuario);
        imobiliaria.setCnpj("12345678000100");
        imobiliaria.setEmail("teste@exemplo.com");
    }

    @Test
    void testContarTotalImobiliariasPorUsuario() {
        when(repository.countByUsuario(usuario)).thenReturn(5L);
        long total = imobiliariaService.contarTotalImobiliariasPorUsuario(usuario);
        assertEquals(5L, total);
    }

    @Test
    void testContarComNegociacoes() {
        when(repository.countByUsuarioAndNegociacoesIsNotEmpty(usuario)).thenReturn(3L);
        assertEquals(3L, imobiliariaService.contarImobiliariasComNegociacoesPorUsuario(usuario));
    }

    @Test
    void testContarSemNegociacoes() {
        when(repository.countByUsuarioAndNegociacoesIsEmpty(usuario)).thenReturn(2L);
        assertEquals(2L, imobiliariaService.contarImobiliariasSemNegociacoesPorUsuario(usuario));
    }

    @Test
    void testDeleteImobiliariaComSucesso() {
        UUID id = imobiliaria.getId();
        when(repository.findById(id)).thenReturn(Optional.of(imobiliaria));
        imobiliariaService.deleteImobiliaria(id);
        verify(repository).delete(imobiliaria);
    }

    @Test
    void testDeleteImobiliariaNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> imobiliariaService.deleteImobiliaria(id));
    }

    @Test
    void testSalvarOuAtualizar_SemDuplicidade() {
        when(repository.findByCnpjAndUsuario(anyString(), any())).thenReturn(Optional.empty());
        when(repository.findByEmailAndUsuario(anyString(), any())).thenReturn(Optional.empty());

        imobiliariaService.salvarOuAtualizar(imobiliaria, usuario);
        verify(repository).save(imobiliaria);
    }

    @Test
    void testSalvarOuAtualizar_ComDuplicidadeCnpj() {
        Imobiliaria duplicada = new Imobiliaria();
        duplicada.setId(UUID.randomUUID());
        when(repository.findByCnpjAndUsuario(anyString(), any())).thenReturn(Optional.of(duplicada));

        assertThrows(IllegalArgumentException.class, () ->
                imobiliariaService.salvarOuAtualizar(imobiliaria, usuario));
    }

    @Test
    void testSalvarOuAtualizar_ComDuplicidadeEmail() {
        when(repository.findByCnpjAndUsuario(anyString(), any())).thenReturn(Optional.empty());

        Imobiliaria duplicada = new Imobiliaria();
        duplicada.setId(UUID.randomUUID());
        when(repository.findByEmailAndUsuario(anyString(), any())).thenReturn(Optional.of(duplicada));

        assertThrows(IllegalArgumentException.class, () ->
                imobiliariaService.salvarOuAtualizar(imobiliaria, usuario));
    }

    @Test
    void testGetById_Sucesso() {
        UUID id = imobiliaria.getId();
        when(repository.findById(id)).thenReturn(Optional.of(imobiliaria));
        Imobiliaria result = imobiliariaService.getById(id);
        assertEquals(imobiliaria, result);
    }

    @Test
    void testGetById_NaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> imobiliariaService.getById(id));
    }

    @Test
    void testFindByUsuario() {
        List<Imobiliaria> mockList = List.of(imobiliaria);
        when(repository.findByUsuario(usuario)).thenReturn(mockList);
        List<Imobiliaria> result = imobiliariaService.findByUsuario(usuario);
        assertEquals(1, result.size());
    }
}
