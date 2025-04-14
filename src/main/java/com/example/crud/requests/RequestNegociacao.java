package com.example.crud.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;


public record RequestNegociacao(

        @JsonIgnore UUID id,
        @NotNull UUID idImovel,
        @NotNull UUID idImobiliaria,
        @NotNull UUID idCliente,

        @NotNull String contatoCliente,
        @NotNull Double valorProposto,
        @NotNull String status,
        @NotNull LocalDate dataNegociacao

) { }
