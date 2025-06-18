package com.example.crud.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record RequestUsuario(

        UUID id,

        @NotBlank
        String nome,

        @NotBlank
        String email,

        @NotBlank
        String senha,

        @NotBlank
        String telefone,

        @NotNull
        LocalDate data_cadastro) {
}
