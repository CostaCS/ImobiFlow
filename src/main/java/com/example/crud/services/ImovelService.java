package com.example.crud.services;

import com.example.crud.domain.entitys.Imovel;
import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.repositorys.ImovelRepository;
import com.example.crud.repositorys.ImobiliariaRepository;
import com.example.crud.requests.RequestImovel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private ImobiliariaRepository imobiliariaRepository;

    public List<Imovel> getAllImoveis() {
        return imovelRepository.findAll();
    }

    @Transactional
    public void registerImovel(RequestImovel request) {

        Imobiliaria imobiliaria = imobiliariaRepository.findById(request.idImobiliaria())
                .orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));


        Imovel imovel = new Imovel(request);
        imovel.setImobiliaria(imobiliaria);


        imovelRepository.save(imovel);
    }

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

    @Transactional
    public void deleteImovel(UUID id) {
        Optional<Imovel> optionalImovel = imovelRepository.findById(id);
        if (optionalImovel.isPresent()) {
            imovelRepository.delete(optionalImovel.get());
        } else {
            throw new EntityNotFoundException("Imóvel não encontrado");
        }
    }
}
