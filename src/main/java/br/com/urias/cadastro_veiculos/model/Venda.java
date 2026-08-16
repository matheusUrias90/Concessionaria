package br.com.urias.cadastro_veiculos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    private LocalDate dataVenda;

    private Double valor;

    @OneToOne
    @JoinColumn(name = "pagamento_id", unique = true)
    private Pagamento pagamento;

    @OneToOne
    @JoinColumn(name = "negociacao_id", unique = true)
    private Negociacao negociacao;

    public Venda() {
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public Double getValor() {
        return valor;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Negociacao getNegociacao() {
        return negociacao;
    }

    public void setNegociacao(Negociacao negociacao) {
        this.negociacao = negociacao;
    }
}
