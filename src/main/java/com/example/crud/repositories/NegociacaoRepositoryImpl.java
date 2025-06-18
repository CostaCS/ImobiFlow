package com.example.crud.repositories;

import com.example.crud.domain.entitys.Negociacao;
import com.example.crud.domain.entitys.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NegociacaoRepositoryImpl implements NegociacaoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Negociacao> filtrarPorFiltros(String cliente, String status,
                                              String imobiliaria, String imovel,
                                              LocalDate data, BigDecimal valorMin, BigDecimal valorMax,
                                              Usuario usuario, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // ---------- Query Principal ----------
        CriteriaQuery<Negociacao> cq = cb.createQuery(Negociacao.class);
        Root<Negociacao> root = cq.from(Negociacao.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("usuario"), usuario));

        if (cliente != null && !cliente.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("cliente").get("nome")), "%" + cliente.toLowerCase() + "%"));
        }
        if (status != null && !status.isBlank()) {
            predicates.add(cb.equal(root.get("status"), status));
        }
        if (imobiliaria != null && !imobiliaria.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("imobiliaria").get("nome")), "%" + imobiliaria.toLowerCase() + "%"));
        }
        if (imovel != null && !imovel.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("imovel").get("titulo")), "%" + imovel.toLowerCase() + "%"));
        }
        if (data != null) {
            predicates.add(cb.equal(root.get("dataNegociacao"), data));
        }
        if (valorMin != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("valorProposto"), valorMin));
        }
        if (valorMax != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("valorProposto"), valorMax));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("dataNegociacao")));

        List<Negociacao> resultado = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // ---------- Count Query ----------
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Negociacao> countRoot = countQuery.from(Negociacao.class);
        countQuery.select(cb.count(countRoot));
        List<Predicate> countPredicates = new ArrayList<>();

        countPredicates.add(cb.equal(countRoot.get("usuario"), usuario));

        if (cliente != null && !cliente.isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("cliente").get("nome")), "%" + cliente.toLowerCase() + "%"));
        }
        if (status != null && !status.isBlank()) {
            countPredicates.add(cb.equal(countRoot.get("status"), status));
        }
        if (imobiliaria != null && !imobiliaria.isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("imobiliaria").get("nome")), "%" + imobiliaria.toLowerCase() + "%"));
        }
        if (imovel != null && !imovel.isBlank()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("imovel").get("titulo")), "%" + imovel.toLowerCase() + "%"));
        }
        if (data != null) {
            countPredicates.add(cb.equal(countRoot.get("dataNegociacao"), data));
        }
        if (valorMin != null) {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("valorProposto"), valorMin));
        }
        if (valorMax != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("valorProposto"), valorMax));
        }

        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultado, pageable, total);
    }
}
