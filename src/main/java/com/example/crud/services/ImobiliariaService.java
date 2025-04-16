package com.example.crud.services;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.repositorys.ImobiliariaRepository;
import com.example.crud.requests.RequestImobiliaria;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImobiliariaService {

    @Autowired
    private ImobiliariaRepository repository;

    public List<Imobiliaria> getAllImobiliarias() {
        return repository.findAll();
    }

    public void registerImobiliaria(RequestImobiliaria data) {
        Imobiliaria newImobiliaria = new Imobiliaria(data);
        repository.save(newImobiliaria);
    }

    @Transactional
    public Imobiliaria updateImobiliaria(RequestImobiliaria data) {
        Optional<Imobiliaria> optionalImobiliaria = repository.findById(data.id());
        if (optionalImobiliaria.isPresent()) {
            Imobiliaria imobiliaria = optionalImobiliaria.get();
            imobiliaria.setNome(data.nome());
            imobiliaria.setCnpj(data.cnpj());
            imobiliaria.setEmail(data.email());
            imobiliaria.setTelefone(data.telefone());
            imobiliaria.setEndereco(data.endereco());
            return imobiliaria;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    public void deleteImobiliaria(UUID id) {
        Optional<Imobiliaria> optionalImobiliaria = repository.findById(id);
        if (optionalImobiliaria.isPresent()) {
            repository.delete(optionalImobiliaria.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Imobiliaria getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));
    }

    public void salvarOuAtualizar(Imobiliaria i) {
        repository.save(i);
    }
}

