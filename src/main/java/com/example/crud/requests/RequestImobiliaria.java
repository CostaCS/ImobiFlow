package com.example.crud.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;


import java.time.LocalDate;

public record RequestImobiliaria(

        UUID id,

        @NotBlank
        String nome,

        @NotBlank
        String cnpj,

        @NotBlank
        String email,

        @NotBlank
        String telefone,

        @NotNull
        String endereco) {
}
