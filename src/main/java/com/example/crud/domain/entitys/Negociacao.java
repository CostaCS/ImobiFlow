package com.example.crud.domain.entitys;

import com.example.crud.requests.RequestNegociacao;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Table(name = "negociacao")
@Entity(name = "Negociacao")
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

    @Column(precision = 15, scale = 2)
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal valorProposto;

    private String status;

    private LocalDate dataNegociacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


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
