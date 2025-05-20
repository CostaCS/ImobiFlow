package com.example.crud.domain.entitys;
import java.time.LocalDate;
import java.util.UUID;

import com.example.crud.requests.RequestImobiliaria;
import jakarta.persistence.*;
import lombok.*;


@Table(name="imobiliaria")
@Entity(name="imobiliaria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Imobiliaria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private String cnpj;

    private String email;

    private String telefone;

    private String endereco;


    public Imobiliaria(RequestImobiliaria requestImobiliaria) {
        this.nome = requestImobiliaria.nome();
        this.cnpj = requestImobiliaria.cnpj();
        this.email = requestImobiliaria.email();
        this.telefone = requestImobiliaria.telefone();
        this.endereco = requestImobiliaria.endereco();
    }
}
