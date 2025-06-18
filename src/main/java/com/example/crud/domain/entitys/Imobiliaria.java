package com.example.crud.domain.entitys;

import com.example.crud.requests.RequestImobiliaria;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Table(name = "imobiliaria")
@Entity
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

    @Column(nullable = false, length = 9)
    private String cep;

    private String endereco;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "imobiliaria", cascade = CascadeType.ALL)
    private List<Negociacao> negociacoes;


    public Imobiliaria(RequestImobiliaria requestImobiliaria) {
        this.nome = requestImobiliaria.nome();
        this.cnpj = requestImobiliaria.cnpj();
        this.email = requestImobiliaria.email();
        this.telefone = requestImobiliaria.telefone();
        this.cep = requestImobiliaria.cep();
        this.endereco = requestImobiliaria.endereco();
    }
}
