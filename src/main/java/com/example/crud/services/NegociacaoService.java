package com.example.crud.services;

import com.example.crud.domain.entitys.*;
import com.example.crud.repositorys.ClienteRepository;
import com.example.crud.repositorys.ImobiliariaRepository;
import com.example.crud.repositorys.ImovelRepository;
import com.example.crud.repositorys.NegociacaoRepository;
import com.example.crud.requests.RequestNegociacao;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
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

    public List<Negociacao> getAllNegociacoes() {
        return repository.findAll();
    }

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

    @Transactional
    public void deleteNegociacao(UUID id) {
        Optional<Negociacao> optionalNegociacao = repository.findById(id);
        if (optionalNegociacao.isPresent()) {
            repository.delete(optionalNegociacao.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Optional<Negociacao> buscarPorId(UUID id) {
        return repository.findById(id);
    }

    //HTML
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

    public Page<Negociacao> buscarPorUsuario(Usuario usuario, Pageable pageable) {
        return repository.findByUsuario(usuario, pageable);
    }

    public Page<Negociacao> buscarPorUsuarioComBusca(String busca, Usuario usuario, Pageable pageable) {
        return repository.findByImobiliariaNomeContainingIgnoreCaseOrClienteNomeContainingIgnoreCaseAndUsuario(
                busca, busca, usuario, pageable
        );
    }

}

