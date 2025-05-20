package com.example.crud.domain.entitys;
import java.time.LocalDate;
import java.util.UUID;

import com.example.crud.requests.RequestNegociacao;
import jakarta.persistence.*;
import lombok.*;


@Table(name="negociacao")
@Entity(name="negociacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Negociacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_imovel", nullable = false)
    private Imovel imovel;

    @ManyToOne
    @JoinColumn(name = "id_imobiliaria", nullable = false)
    private Imobiliaria imobiliaria;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    private String contatoCliente;

    private Double valorProposto;

    private String status;

    private LocalDate dataNegociacao;


    public Negociacao(RequestNegociacao request) {
        this.contatoCliente = request.contatoCliente();
        this.valorProposto = request.valorProposto();
        this.status = request.status();
        this.dataNegociacao = request.dataNegociacao();
        this.imovel = null;
        this.imobiliaria = null;
        this.cliente = null;
    }

}
