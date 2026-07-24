package br.com.urias.cadastro_veiculos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private Double entrada;

    private Integer parcelas;

    private Double valorParcela;

    private String instituicaoFinanceira;

    public Pagamento() {
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Double getEntrada() {
        return entrada;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public Double getValorParcela() {
        return valorParcela;
    }

    public String getInstituicaoFinanceira() {
        return instituicaoFinanceira;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEntrada(Double entrada) {
        this.entrada = entrada;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public void setValorParcela(Double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public void setInstituicaoFinanceira(String instituicaoFinanceira) {
        this.instituicaoFinanceira = instituicaoFinanceira;
    }
}