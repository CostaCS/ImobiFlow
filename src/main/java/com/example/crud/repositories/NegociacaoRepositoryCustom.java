package com.example.crud.repositories;

import com.example.crud.domain.entitys.Negociacao;
import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface NegociacaoRepositoryCustom {
    Page<Negociacao> filtrarPorFiltros(
            String cliente,
            String status,
            String imobiliaria,
            String imovel,
            LocalDate data,
            BigDecimal valorMin,
            BigDecimal valorMax,
            Usuario usuario,
            Pageable pageable
    );
}
