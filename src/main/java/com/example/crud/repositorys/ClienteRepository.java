package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Cliente;
import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.crud.domain.entitys.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Page<Cliente> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(String nome, String email, Pageable pageable);

    Optional<Cliente> findByEmailIgnoreCase(String email);

    List<Cliente> findByUsuario(Usuario usuario);

    Page<Cliente> findByUsuario(Usuario usuario, Pageable pageable);

    Page<Cliente> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseAndUsuario(
            String nome, String email, Usuario usuario, Pageable pageable);

}