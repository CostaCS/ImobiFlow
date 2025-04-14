package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Negociacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface NegociacaoRepository extends JpaRepository<Negociacao, UUID> {

}
