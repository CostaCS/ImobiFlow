package com.example.crud.services;

import com.example.crud.domain.entitys.*;
import com.example.crud.repositories.ClienteRepository;
import com.example.crud.repositories.ImobiliariaRepository;
import com.example.crud.repositories.ImovelRepository;
import com.example.crud.repositories.NegociacaoRepository;
import com.example.crud.requests.RequestNegociacao;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NegociacaoService {

    @Autowired
    private NegociacaoRepository repository;

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private ImobiliariaRepository imobiliariaRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    // Retorna todas as negociações cadastradas no banco de dados via JSON
    public List<Negociacao> getAllNegociacoes() {
        return repository.findAll();
    }


    // Registra uma nova negociação no sistema, vinculando cliente, imóvel e imobiliária informados na requisição, via JSON
    public void registerNegociacao(RequestNegociacao request) {
        Imovel imovel = imovelRepository.findById(request.idImovel())
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado"));

        Imobiliaria imobiliaria = imobiliariaRepository.findById(request.idImobiliaria())
                .orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));

        Cliente cliente = clienteRepository.findById(request.idCliente())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Negociacao negociacao = new Negociacao(request);

        negociacao.setImovel(imovel);
        negociacao.setImobiliaria(imobiliaria);
        negociacao.setCliente(cliente);

        repository.save(negociacao);
    }

    // Atualiza dos dados de negociação, via JSON
    public Negociacao updateNegociacao(UUID id, RequestNegociacao data) {
        Negociacao negociacao = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Negociação não encontrada"));

        Imovel imovel = imovelRepository.findById(data.idImovel())
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado"));

        Imobiliaria imobiliaria = imobiliariaRepository.findById(data.idImobiliaria())
                .orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));

        Cliente cliente = clienteRepository.findById(data.idCliente())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        negociacao.setImovel(imovel);
        negociacao.setImobiliaria(imobiliaria);
        negociacao.setCliente(cliente);

        negociacao.setContatoCliente(data.contatoCliente());
        negociacao.setValorProposto(data.valorProposto());
        negociacao.setStatus(data.status());
        negociacao.setDataNegociacao(data.dataNegociacao());

        return repository.save(negociacao);
    }

    // Deleta uma negociação do sistema via JSON
    @Transactional
    public void deleteNegociacao(UUID id) {
        Optional<Negociacao> optionalNegociacao = repository.findById(id);
        if (optionalNegociacao.isPresent()) {
            repository.delete(optionalNegociacao.get());
        } else {
            throw new EntityNotFoundException();
        }
    }


    //Salva uma negociação no sistema na página html pelo usuário informado
    public void salvarViaHTML(Negociacao negociacao, Usuario usuario) {

        UUID idCliente = negociacao.getCliente().getId();
        UUID idImovel = negociacao.getImovel().getId();
        UUID idImobiliaria = negociacao.getImobiliaria().getId();

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Imovel imovel = imovelRepository.findById(idImovel)
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado"));

        Imobiliaria imobiliaria = imobiliariaRepository.findById(idImobiliaria)
                .orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));

        negociacao.setCliente(cliente);
        negociacao.setImovel(imovel);
        negociacao.setImobiliaria(imobiliaria);

        if (negociacao.getDataNegociacao() == null) {
            negociacao.setDataNegociacao(LocalDate.now());
        }

        negociacao.setUsuario(usuario);
        repository.save(negociacao);
    }

    // Retorna a quantidade total de negociações registradas pelo usuário informado
    public long contarTodas(Usuario usuario) {
        return repository.countByUsuario(usuario);
    }

    // Retorna a imobliaria com mais negociações vinculadas pelo usuário informado
    public String imobiliariaComMaisNegociacoes(Usuario usuario) {
        return repository.imobiliariaComMaisNegociacoes(usuario.getId());
    }

    // Retorna a quantidade de negociações por cada status
    public long contarPorStatus(String status, Usuario usuario) {
        return repository.countByStatusAndUsuario(status, usuario);
    }

    // Aplica filtros dinâmicos nas negociações com base nos parâmetros informados e retorna os resultados paginados
    public Page<Negociacao> filtrarNegociacoes(
            String cliente,
            String status,
            String imobiliariaId,
            String imovelId,
            LocalDate data,
            BigDecimal valorMin,
            BigDecimal valorMax,
            Usuario usuario,
            Pageable pageable
    ) {
        return repository.filtrarPorFiltros(cliente, status, imobiliariaId, imovelId, data, valorMin, valorMax, usuario, pageable);
    }

    public Negociacao buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Negociação não encontrada"));
    }

    public long calcularDiasParado(Negociacao negociacao) {
        if (negociacao.getDataNegociacao() == null) return 0;
        return ChronoUnit.DAYS.between(negociacao.getDataNegociacao(), LocalDate.now());
    }


}

