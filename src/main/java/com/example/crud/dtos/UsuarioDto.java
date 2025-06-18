package com.example.crud.dtos;

import com.example.crud.domain.entitys.Usuario;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsuarioDto {
    private UUID id;

    @NotBlank(message = "Nome não pode ficar vazio")
    private String nome;

    @Setter(AccessLevel.NONE)
    private String email;

    private String telefone;


    public static UsuarioDto fromEntity(Usuario u) {
        var dto = new UsuarioDto();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.email = u.getEmail();
        dto.setTelefone(u.getTelefone());
        return dto;
    }


    public void applyTo(Usuario u) {
        u.setNome(this.nome);
        u.setTelefone(this.telefone);

    }
}
