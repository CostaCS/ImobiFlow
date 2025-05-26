package com.example.crud.repositories;

import com.example.crud.domain.entitys.Imovel;
import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ImovelRepository extends JpaRepository<Imovel, UUID> {

    // Busca com filtro por usuário
    Page<Imovel> findByUsuarioAndTituloContainingIgnoreCaseOrUsuarioAndEnderecoContainingIgnoreCase(
            Usuario usuario1, String titulo,
            Usuario usuario2, String endereco,
            Pageable pageable);

    // Filtros simples por usuário
    Page<Imovel> findByUsuario(Usuario usuario, Pageable pageable);

    /* Contagens
    long countByUsuario(Usuario usuario);

    long countByUsuarioAndNegociacoesIsNotEmpty(Usuario usuario);

    long countByUsuarioAndNegociacoesIsEmpty(Usuario usuario);
    */

    List<Imovel> findByUsuario(Usuario usuario);

    @Query("SELECT i.tipoImovel, COUNT(i) FROM imovel i WHERE i.usuario = :usuario GROUP BY i.tipoImovel")
    List<Object[]> countImoveisByTipoPorUsuario(@Param("usuario") Usuario usuario);

    long countByUsuario(Usuario usuario);

    long countByStatusAndUsuario(String status, Usuario usuario);

}

