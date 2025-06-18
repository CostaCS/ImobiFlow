package com.example.crud.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenhaDto {


    @Size(min = 5, message = "Nova senha precisa ter ao menos 6 caracteres")
    private String newPassword;

    @NotBlank(message = "Confirme a nova senha")
    private String confirmPassword;

}
