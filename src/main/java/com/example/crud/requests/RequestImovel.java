package com.example.crud.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public record RequestImovel(


        @NotNull String titulo,
        @NotNull String descricao,
        @NotBlank(message = "O CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Formato de CEP inválido")
        String cep,
        @NotNull String endereco,
        @NotNull String tipoImovel,
        @NotNull Double metragem,
        @NotNull BigDecimal preco,
        @NotNull String status,
        @NotNull LocalDate dataCadastro,
        @NotNull UUID idImobiliaria,
        @NotNull BigDecimal precoCondominio
) {
}
