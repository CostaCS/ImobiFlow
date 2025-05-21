package com.example.crud.domain.entitys;

import java.util.UUID;
import com.example.crud.requests.RequestCliente;
import jakarta.persistence.*;
import lombok.*;


@Table(name="cliente")
@Entity(name="cliente")
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

    private String endereco;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    public Cliente(RequestCliente requestCliente) {
        this.nome = requestCliente.nome();
        this.email = requestCliente.email();
        this.telefone = requestCliente.telefone();
        this.endereco = requestCliente.endereco();
    }
}

