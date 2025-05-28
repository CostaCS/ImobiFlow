package com.example.crud.repositories;

import com.example.crud.domain.entitys.Negociacao;
import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NegociacaoRepository extends JpaRepository<Negociacao, UUID>, NegociacaoRepositoryCustom {

    Page<Negociacao> findByUsuario(Usuario usuario, Pageable pageable);

    Page<Negociacao> findByImobiliariaNomeContainingIgnoreCaseOrClienteNomeContainingIgnoreCaseAndUsuario(
            String nomeImobiliaria, String nomeCliente, Usuario usuario, Pageable pageable
    );

    long countByUsuario(Usuario usuario);

    Negociacao findTopByUsuarioOrderByDataNegociacaoDesc(Usuario usuario);

    long countByStatusAndUsuario(String status, Usuario usuario);

    @Query("SELECT n.status, COUNT(n) FROM Negociacao n WHERE n.usuario.id = :usuarioId GROUP BY n.status")
    List<Object[]> contarPorStatus(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT TO_CHAR(n.dataNegociacao, 'YYYY-MM') AS mes, COUNT(n) " +
            "FROM Negociacao n " +
            "WHERE n.usuario.id = :usuarioId " +
            "GROUP BY mes " +
            "ORDER BY mes")
    List<Object[]> contarPorMes(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT n.status, COUNT(n) FROM Negociacao n WHERE n.usuario = :usuario GROUP BY n.status")
    List<Object[]> contarPorStatusTodos(@Param("usuario") Usuario usuario);

    @Query("SELECT n.imobiliaria.nome FROM Negociacao n WHERE n.usuario.id = :usuarioId GROUP BY n.imobiliaria.nome ORDER BY COUNT(n) DESC LIMIT 1")
    String imobiliariaComMaisNegociacoes(@Param("usuarioId") UUID usuarioId);

    @Query("""
      SELECT n.cliente.nome     AS nome,
             COUNT(n)           AS total
      FROM Negociacao n
      WHERE n.usuario.id = :usuarioId
      GROUP BY n.cliente.nome
      ORDER BY total DESC
    """)
    List<Object[]> findTopClientes(
            @Param("usuarioId") UUID usuarioId,
            Pageable pageable
    );
}
