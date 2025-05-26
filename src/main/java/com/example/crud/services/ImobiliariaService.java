package com.example.crud.services;

import com.example.crud.domain.entitys.Imobiliaria;
import com.example.crud.domain.entitys.Usuario;
import com.example.crud.repositories.ImobiliariaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImobiliariaService {

    @Autowired
    private ImobiliariaRepository repository;


    // Para uso com user-tenant (filtrando por usuário logado)
    public Page<Imobiliaria> getAllByUsuario(Usuario usuario, Pageable pageable) {
        return repository.findByUsuario(usuario, pageable);
    }

    public Page<Imobiliaria> buscarPorNomeOuCnpjEUsuario(String busca, Usuario usuario, Pageable pageable) {
        return repository.findByUsuarioAndNomeContainingIgnoreCaseOrUsuarioAndCnpjContainingIgnoreCase(
                usuario, busca, usuario, busca, pageable);
    }

    public long contarTotalImobiliariasPorUsuario(Usuario usuario) {
        return repository.countByUsuario(usuario);
    }

    public long contarImobiliariasComNegociacoesPorUsuario(Usuario usuario) {
        return repository.countByUsuarioAndNegociacoesIsNotEmpty(usuario);
    }

    public long contarImobiliariasSemNegociacoesPorUsuario(Usuario usuario) {
        return repository.countByUsuarioAndNegociacoesIsEmpty(usuario);
    }

    // Métodos antigos (sem filtro por usuário)
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
            throw new EntityNotFoundException("Imobiliária não encontrada");
        }
    }

    public Imobiliaria getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Imobiliária não encontrada"));
    }

    public void salvarOuAtualizar(Imobiliaria imobiliaria, Usuario usuario) {
        // Verifica duplicidade de CNPJ para o mesmo usuário
        repository.findByCnpjAndUsuario(imobiliaria.getCnpj(), usuario).ifPresent(i -> {
            if (!i.getId().equals(imobiliaria.getId())) {
                throw new IllegalArgumentException("CNPJ já cadastrado.");
            }
        });

        // Verifica duplicidade de E-mail para o mesmo usuário
        repository.findByEmailAndUsuario(imobiliaria.getEmail(), usuario).ifPresent(i -> {
            if (!i.getId().equals(imobiliaria.getId())) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }
        });

        imobiliaria.setUsuario(usuario); // associação obrigatória
        repository.save(imobiliaria);
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

    public List<Imobiliaria> findByUsuario(Usuario usuario) {
        System.out.println("Buscando imobiliárias para o usuário: " + usuario.getId());
        List<Imobiliaria> resultado = repository.findByUsuario(usuario);
        System.out.println("Quantidade retornada: " + resultado.size());
        return resultado;
    }

    public Page<Imobiliaria> buscarComFiltros(String busca, String telefone, String email, Usuario usuario, Pageable pageable) {
        return repository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("usuario"), usuario));

            if (busca != null && !busca.isBlank()) {
                Predicate nomePredicate = cb.like(cb.lower(root.get("nome")), "%" + busca.toLowerCase() + "%");
                Predicate cnpjPredicate = cb.like(cb.lower(root.get("cnpj")), "%" + busca.toLowerCase() + "%");
                predicates.add(cb.or(nomePredicate, cnpjPredicate));
            }

            if (telefone != null && !telefone.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("telefone")), "%" + telefone.toLowerCase() + "%"));
            }

            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }


}
