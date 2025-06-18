package com.example.crud.services;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.domain.entitys.Imovel;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.ImobiliariaRepository;
import com.example.crud.repositories.ImovelRepository;
import com.example.crud.requests.RequestImovel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private ImobiliariaRepository imobiliariaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Retorna todos os imóveis cadastrados no sistema
    public List<Imovel> getAllImoveis() {
        return imovelRepository.findAll();
    }

    //Registra um novo imóvel no sistema, associando a uma imobiliária existente
    @Transactional
    public void registerImovel(RequestImovel request) {
        Imobiliaria imobiliaria = imobiliariaRepository.findById(request.idImobiliaria())
                .orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));
        Imovel imovel = new Imovel(request);
        imovel.setImobiliaria(imobiliaria);
        imovelRepository.save(imovel);
    }

    //Atualiza os dados de um imóvel existente com base no ID informado
    @Transactional
    public Imovel updateImovel(UUID id, RequestImovel request) {
        Imovel imovel = imovelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado"));
        imovel.setTitulo(request.titulo());
        imovel.setDescricao(request.descricao());
        imovel.setEndereco(request.endereco());
        imovel.setTipoImovel(request.tipoImovel());
        imovel.setMetragem(request.metragem());
        imovel.setPreco(request.preco());
        imovel.setStatus(request.status());
        imovel.setDataCadastro(request.dataCadastro());
        Imobiliaria imobiliaria = imobiliariaRepository.findById(request.idImobiliaria())
                .orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));
        imovel.setImobiliaria(imobiliaria);
        return imovel;
    }

    //  Exclui um imóvel do sistema com base no ID informado
    @Transactional
    public void deleteImovel(UUID id) {
        Optional<Imovel> optionalImovel = imovelRepository.findById(id);
        if (optionalImovel.isPresent()) {
            imovelRepository.delete(optionalImovel.get());
        } else {
            throw new EntityNotFoundException("Imóvel não encontrado");
        }
    }

    // HTML
    //Salva um imóvel via formulário HTML, associando ao usuário logado e definindo a data de cadastro
    public void salvarViaHTML(Imovel imovel, Usuario usuario) {
        imovel.setDataCadastro(LocalDate.now());
        UUID idImobiliaria = imovel.getImobiliaria().getId();
        Imobiliaria imobiliaria = imobiliariaRepository.findById(idImobiliaria)
                .orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));
        imovel.setImobiliaria(imobiliaria);
        imovel.setUsuario(usuario);
        imovelRepository.save(imovel);
    }

    //Realiza busca paginada de imóveis do usuário, com filtro por título ou endereço
    public Page<Imovel> buscarPorUsuario(String busca, Usuario usuario, Pageable pageable) {
        if (busca != null && !busca.isEmpty()) {
            return imovelRepository
                    .findByUsuarioAndTituloContainingIgnoreCaseOrUsuarioAndEnderecoContainingIgnoreCase(
                            usuario, busca, usuario, busca, pageable
                    );
        } else {
            return imovelRepository.findByUsuario(usuario, pageable);
        }
    }

    //Retorna todos os imóveis cadastrados por um usuário (sem paginação)
    public List<Imovel> findByUsuario(Usuario usuario) {
        return imovelRepository.findByUsuario(usuario);
    }

    //Conta o número de imóveis com um determinado status para um usuário específico
    public long contarPorStatus(String status, Usuario usuario) {
        return imovelRepository.countByStatusAndUsuario(status, usuario);
    }

    //Aplica filtros avançados nos imóveis com base em múltiplos critérios e retorna os resultados paginados
    public Page<Imovel> filtrarImoveis(
            String busca,
            String tipo,
            BigDecimal precoMin,
            BigDecimal precoMax,
            Integer quartos,
            Integer banheiros,
            Integer vagas,
            String status,
            UUID imobiliariaId,
            String descricao,
            String endereco,
            BigDecimal precoCondominioMin,
            BigDecimal precoCondominioMax,
            Usuario usuario,
            Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // consulta de conteúdo
        CriteriaQuery<Imovel> contentCQ = cb.createQuery(Imovel.class);
        Root<Imovel> root = contentCQ.from(Imovel.class);

        List<Predicate> preds = buildPredicates(cb, root,
                busca, tipo, precoMin, precoMax, quartos, banheiros, vagas,
                status, imobiliariaId, descricao, endereco, precoCondominioMin, precoCondominioMax, usuario);

        contentCQ.where(preds.toArray(new Predicate[0]))
                .orderBy(cb.desc(root.get("dataCadastro")));

        List<Imovel> lista = entityManager.createQuery(contentCQ)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // consulta de total
        CriteriaQuery<Long> countCQ = cb.createQuery(Long.class);
        Root<Imovel> countRoot = countCQ.from(Imovel.class);
        List<Predicate> countPreds = buildPredicates(cb, countRoot,
                busca, tipo, precoMin, precoMax, quartos, banheiros, vagas,
                status, imobiliariaId, descricao, endereco, precoCondominioMin, precoCondominioMax, usuario);

        countCQ.select(cb.count(countRoot))
                .where(countPreds.toArray(new Predicate[0]));

        long total = entityManager.createQuery(countCQ).getSingleResult();

        return new PageImpl<>(lista, pageable, total);
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<Imovel> root,
            String busca,
            String tipo,
            BigDecimal precoMin,
            BigDecimal precoMax,
            Integer quartos,
            Integer banheiros,
            Integer vagas,
            String status,
            UUID imobiliariaId,
            String descricao,
            String endereco,
            BigDecimal precoCondominioMin,
            BigDecimal precoCondominioMax,
            Usuario usuario) {

        List<Predicate> p = new ArrayList<>();

        // filtro obrigatório de tenant
        p.add(cb.equal(root.get("usuario"), usuario));

        // título
        if (busca != null && !busca.isBlank()) {
            p.add(cb.like(cb.lower(root.get("titulo")), "%" + busca.toLowerCase() + "%"));
        }
        // descrição
        if (descricao != null && !descricao.isBlank()) {
            p.add(cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%"));
        }
        // endereço
        if (endereco != null && !endereco.isBlank()) {
            p.add(cb.like(cb.lower(root.get("endereco")), "%" + endereco.toLowerCase() + "%"));
        }
        // tipo
        if (tipo != null && !tipo.isBlank()) {
            p.add(cb.equal(root.get("tipoImovel"), tipo));
        }
        // preço
        if (precoMin != null) {
            p.add(cb.greaterThanOrEqualTo(root.get("preco"), precoMin));
        }
        if (precoMax != null) {
            p.add(cb.lessThanOrEqualTo(root.get("preco"), precoMax));
        }
        // condomínio
        if (precoCondominioMin != null) {
            p.add(cb.greaterThanOrEqualTo(root.get("precoCondominio"), precoCondominioMin));
        }
        if (precoCondominioMax != null) {
            p.add(cb.lessThanOrEqualTo(root.get("precoCondominio"), precoCondominioMax));
        }
        // quartos, banheiros, vagas
        if (quartos != null) {
            p.add(cb.equal(root.get("quartos"), quartos));
        }
        if (banheiros != null) {
            p.add(cb.equal(root.get("banheiros"), banheiros));
        }
        if (vagas != null) {
            p.add(cb.equal(root.get("vagas"), vagas));
        }
        // status
        if (status != null && !status.isBlank()) {
            p.add(cb.equal(root.get("status"), status));
        }
        // imobiliária
        if (imobiliariaId != null) {
            p.add(cb.equal(root.get("imobiliaria").get("id"), imobiliariaId));
        }

        return p;
    }

}
