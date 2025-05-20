package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Page<Cliente> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(String nome, String email, Pageable pageable);
    Optional<Cliente> findByEmailIgnoreCase(String email);
}