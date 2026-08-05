package br.com.urias.cadastro_veiculos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class PendenciaNegociacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "negociacao_id")
    private Negociacao negociacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPendencia tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPendencia status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataResolucao;

    @Column(length = 500)
    private String observacaoResolucao;

    @ManyToOne
    @JoinColumn(name = "resolvida_por_id")
    private Usuario resolvidaPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Negociacao getNegociacao() {
        return negociacao;
    }

    public void setNegociacao(Negociacao negociacao) {
        this.negociacao = negociacao;
    }

    public TipoPendencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoPendencia tipo) {
        this.tipo = tipo;
    }

    public StatusPendencia getStatus() {
        return status;
    }

    public void setStatus(StatusPendencia status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataResolucao() {
        return dataResolucao;
    }

    public void setDataResolucao(LocalDateTime dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    public String getObservacaoResolucao() {
        return observacaoResolucao;
    }

    public void setObservacaoResolucao(String observacaoResolucao) {
        this.observacaoResolucao = observacaoResolucao;
    }

    public Usuario getResolvidaPor() {
        return resolvidaPor;
    }

    public void setResolvidaPor(Usuario resolvidaPor) {
        this.resolvidaPor = resolvidaPor;
    }
}