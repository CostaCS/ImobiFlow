package com.example.crud.domain.entitys;

import com.example.crud.requests.RequestCliente;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Table(name = "cliente")
@Entity(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private String email;

    private String telefone;

    @Column(nullable = false, length = 9)
    private String cep;

    private String endereco;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "imobiliaria_id", nullable = true)
    private Imobiliaria imobiliaria;


    public Cliente(RequestCliente requestCliente) {
        this.nome = requestCliente.nome();
        this.email = requestCliente.email();
        this.telefone = requestCliente.telefone();
        this.cep = requestCliente.cep();
        this.endereco = requestCliente.endereco();
    }
}

