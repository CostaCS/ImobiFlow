package com.example.crud.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;


public record RequestImovel(


        @NotNull String titulo,
        @NotNull String descricao,
        @NotNull String endereco,
        @NotNull String tipoImovel,
        @NotNull Double metragem,
        @NotNull Double preco,
        @NotNull String status,
        @NotNull LocalDate dataCadastro,
        @NotNull UUID idImobiliaria

) { }
