package com.example.crud.domain.entitys;

import java.time.LocalDate;
import java.util.UUID;
import com.example.crud.requests.RequestImovel;
import jakarta.persistence.*;
import lombok.*;


@Table(name="imovel")
@Entity(name="imovel")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String titulo;

    private String descricao;

    private String endereco;

    private String tipoImovel;

    private Double metragem;

    private Double preco;

    private String status;

    @ManyToOne
    @JoinColumn(name = "idImobiliaria", nullable = false)
    private Imobiliaria imobiliaria;

    @Column(name = "dataCadastro")
    private LocalDate dataCadastro;

    public Imovel(RequestImovel request) {
        this.titulo = request.titulo();
        this.descricao = request.descricao();
        this.endereco = request.endereco();
        this.tipoImovel = request.tipoImovel();
        this.metragem = request.metragem();
        this.preco = request.preco();
        this.status = request.status();
        this.dataCadastro = request.dataCadastro();
        this.imobiliaria = null;
    }


    public void setImobiliaria(Imobiliaria imobiliaria) {
        this.imobiliaria = imobiliaria;
    }

}
