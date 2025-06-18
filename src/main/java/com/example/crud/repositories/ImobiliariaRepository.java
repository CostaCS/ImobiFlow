package com.example.crud.repositories;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImobiliariaRepository extends JpaRepository<Imobiliaria, UUID>, JpaSpecificationExecutor<Imobiliaria> {

    void deleteAllByUsuarioId(UUID usuarioId);

    // Busca global (sem filtro de usuário)
    Page<Imobiliaria> findByNomeContainingIgnoreCaseOrCnpjContainingIgnoreCase(String nome, String cnpj, Pageable pageable);

    // Busca filtrada por usuário (nome OU CNPJ)
    Page<Imobiliaria> findByUsuarioAndNomeContainingIgnoreCaseOrUsuarioAndCnpjContainingIgnoreCase(
            Usuario usuario1, String nome,
            Usuario usuario2, String cnpj,
            Pageable pageable);

    // Filtros simples por usuário
    Page<Imobiliaria> findByUsuario(Usuario usuario, Pageable pageable);

    Page<Imobiliaria> findByNomeContainingIgnoreCaseAndUsuario(String nome, Usuario usuario, Pageable pageable);

    // Contagens gerais (sem filtro de usuário)
    @Query("SELECT COUNT(DISTINCT i) FROM Negociacao n JOIN n.imobiliaria i")
    long countImobiliariasComNegociacoes();

    @Query("SELECT COUNT(i) FROM Imobiliaria i WHERE i.id NOT IN (SELECT DISTINCT n.imobiliaria.id FROM Negociacao n)")
    long countImobiliariasSemNegociacoes();

    // Contagens filtradas por usuário
    long countByUsuario(Usuario usuario);

    long countByUsuarioAndNegociacoesIsNotEmpty(Usuario usuario);

    long countByUsuarioAndNegociacoesIsEmpty(Usuario usuario);

    List<Imobiliaria> findByUsuario(Usuario usuario);

    Optional<Imobiliaria> findByCnpjAndUsuario(String cnpj, Usuario usuario);

    Optional<Imobiliaria> findByEmailAndUsuario(String email, Usuario usuario);


}
