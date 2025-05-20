package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Imobiliaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ImobiliariaRepository extends JpaRepository<Imobiliaria, UUID> {
    Page<Imobiliaria> findByNomeContainingIgnoreCaseOrCnpjContainingIgnoreCase(String nome, String cnpj, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT i) FROM negociacao n JOIN n.imobiliaria i")
    long countImobiliariasComNegociacoes();

    @Query("SELECT COUNT(i) FROM imobiliaria i WHERE i.id NOT IN (SELECT DISTINCT n.imobiliaria.id FROM negociacao n)")
    long countImobiliariasSemNegociacoes();

}