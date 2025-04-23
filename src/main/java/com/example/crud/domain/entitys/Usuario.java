package com.example.crud.domain.entitys;
import java.time.LocalDate;
import java.util.UUID;

import com.example.crud.requests.RequestUsuario;
import jakarta.persistence.*;
import lombok.*;


@Table(name="usuario")
@Entity(name="usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private String email;

    private String senha;

    private String telefone;

    private LocalDate data_cadastro;

    @Column(name = "token_recuperacao_senha")
    private String tokenRecuperacaoSenha;


    public Usuario(RequestUsuario requestUsuario) {
        this.nome = requestUsuario.nome();
        this.email = requestUsuario.email();
        this.senha = requestUsuario.senha();
        this.telefone = requestUsuario.telefone();
        this.data_cadastro = requestUsuario.data_cadastro();
   }
}
