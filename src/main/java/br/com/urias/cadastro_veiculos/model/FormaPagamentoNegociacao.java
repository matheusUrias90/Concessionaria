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
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class FormaPagamentoNegociacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "negociacao_id")
    private Negociacao negociacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFormaPagamento tipo;

    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFormaPagamento status;

    private LocalDate dataPrevista;

    private Integer parcelas;

    private Double valorParcela;

    private String instituicaoFinanceira;

    private String administradoraConsorcio;

    private String numeroCartaCredito;

    private String bandeiraCartao;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "veiculo_entrada_id", unique = true)
    private VeiculoEntrada veiculoEntrada;

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

    public TipoFormaPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoFormaPagamento tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public StatusFormaPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusFormaPagamento status) {
        this.status = status;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public Double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(Double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getInstituicaoFinanceira() {
        return instituicaoFinanceira;
    }

    public void setInstituicaoFinanceira(String instituicaoFinanceira) {
        this.instituicaoFinanceira = instituicaoFinanceira;
    }

    public String getAdministradoraConsorcio() {
        return administradoraConsorcio;
    }

    public void setAdministradoraConsorcio(String administradoraConsorcio) {
        this.administradoraConsorcio = administradoraConsorcio;
    }

    public String getNumeroCartaCredito() {
        return numeroCartaCredito;
    }

    public void setNumeroCartaCredito(String numeroCartaCredito) {
        this.numeroCartaCredito = numeroCartaCredito;
    }

    public String getBandeiraCartao() {
        return bandeiraCartao;
    }

    public void setBandeiraCartao(String bandeiraCartao) {
        this.bandeiraCartao = bandeiraCartao;
    }

    public VeiculoEntrada getVeiculoEntrada() {
        return veiculoEntrada;
    }

    public void setVeiculoEntrada(VeiculoEntrada veiculoEntrada) {
        this.veiculoEntrada = veiculoEntrada;
    }
}