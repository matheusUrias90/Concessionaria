package br.com.urias.cadastro_veiculos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pagamentos_repasse")
public class PagamentoRepasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "repasse_id", nullable = false)
    private Repasse repasse;

    @Column(nullable = false)
    private LocalDate dataPagamento;

    @Column(nullable = false)
    private String formaPagamento;

    @Column(nullable = false)
    private Double valor;

    @Column(length = 500)
    private String observacao;

    public PagamentoRepasse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Repasse getRepasse() {
        return repasse;
    }

    public void setRepasse(Repasse repasse) {
        this.repasse = repasse;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}