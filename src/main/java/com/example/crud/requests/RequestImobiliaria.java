package com.example.crud.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

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

        @NotBlank(message = "O CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Formato de CEP inválido")
        String cep,

        @NotNull
        String endereco) {
}
