package com.example.crud.repositorys;

import com.example.crud.domain.entitys.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;


public interface ImovelRepository extends JpaRepository<Imovel, UUID> {

    @Query("SELECT i.tipoImovel, COUNT(i) FROM imovel i GROUP BY i.tipoImovel")
    List<Object[]> countImoveisByTipo();

}
