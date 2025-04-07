package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Imobiliaria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ImobiliariaRepository extends JpaRepository<Imobiliaria, UUID> {
}