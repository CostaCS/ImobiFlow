package com.example.crud.services;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.repositorys.ImobiliariaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImobiliariaService {

    @Autowired
    private ImobiliariaRepository repository;

    public Page<Imobiliaria> getAllImobiliarias(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Imobiliaria> getAllImobiliarias() {
        return repository.findAll();
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
        try {
            repository.save(i);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("CNPJ ou E-mail já cadastrado.");
        }
    }

    public Page<Imobiliaria> buscarPorNomeOuCnpj(String termo, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCaseOrCnpjContainingIgnoreCase(termo, termo, pageable);
    }

    public long contarTotalImobiliarias() {
        return repository.count();
    }

    public long contarImobiliariasComNegociacoes() {
        return repository.countImobiliariasComNegociacoes();
    }

    public long contarImobiliariasSemNegociacoes() {
        return repository.countImobiliariasSemNegociacoes();
    }


}

