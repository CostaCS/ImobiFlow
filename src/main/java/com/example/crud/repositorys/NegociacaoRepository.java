package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Negociacao;
import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface NegociacaoRepository extends JpaRepository<Negociacao, UUID> {


    Page<Negociacao> findByUsuario(Usuario usuario, Pageable pageable);

    Page<Negociacao> findByImobiliariaNomeContainingIgnoreCaseOrClienteNomeContainingIgnoreCaseAndUsuario(
            String nomeImobiliaria, String nomeCliente, Usuario usuario, Pageable pageable
    );

    @Query("SELECT n.status, COUNT(n) FROM Negociacao n WHERE n.usuario = :usuario GROUP BY n.status")
    List<Object[]> countNegociacoesByStatusPorUsuario(@Param("usuario") Usuario usuario);


    long countByUsuario(Usuario usuario);

    Negociacao findTopByUsuarioOrderByDataNegociacaoDesc(Usuario usuario);


}
