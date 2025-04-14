package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}