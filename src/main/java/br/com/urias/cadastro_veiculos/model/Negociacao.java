package br.com.urias.cadastro_veiculos.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Negociacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    private LocalDateTime dataEncerramento;

    @Column(nullable = false)
    private Double valorProposto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusNegociacao status;

    @Column(length = 1000)
    private String observacao;

    @Column(length = 500)
    private String motivoCancelamento;

    @OneToMany(
            mappedBy = "negociacao",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PendenciaNegociacao> pendencias = new ArrayList<>();

    public void adicionarPendencia(PendenciaNegociacao pendencia) {
        pendencias.add(pendencia);
        pendencia.setNegociacao(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(LocalDateTime dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public Double getValorProposto() {
        return valorProposto;
    }

    public void setValorProposto(Double valorProposto) {
        this.valorProposto = valorProposto;
    }

    public StatusNegociacao getStatus() {
        return status;
    }

    public void setStatus(StatusNegociacao status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public List<PendenciaNegociacao> getPendencias() {
        return pendencias;
    }

    public void setPendencias(List<PendenciaNegociacao> pendencias) {
        this.pendencias = pendencias;
    }
}