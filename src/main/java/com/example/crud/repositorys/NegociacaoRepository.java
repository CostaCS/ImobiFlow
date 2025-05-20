package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Negociacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;


public interface NegociacaoRepository extends JpaRepository<Negociacao, UUID> {

    @Query("SELECT n.status, COUNT(n) FROM negociacao n GROUP BY n.status")
    List<Object[]> countNegociacoesByStatus();

    Negociacao findTopByOrderByDataNegociacaoDesc();

}
