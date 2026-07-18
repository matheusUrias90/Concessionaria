package br.com.urias.cadastro_veiculos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "repasses")
public class Repasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "venda_id", nullable = false, unique = true)
    private Venda venda;

    @Column(nullable = false)
    private Double valorDevido;

    @OneToMany(
            mappedBy = "repasse",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PagamentoRepasse> pagamentos = new ArrayList<>();

    public Repasse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Double getValorDevido() {
        return valorDevido;
    }

    public void setValorDevido(Double valorDevido) {
        this.valorDevido = valorDevido;
    }

    public List<PagamentoRepasse> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<PagamentoRepasse> pagamentos) {
        this.pagamentos = pagamentos;
    }

    @Transient
    public double getTotalPago() {
        if (pagamentos == null) {
            return 0.0;
        }

        return pagamentos.stream()
                .filter(p -> p.getValor() != null)
                .mapToDouble(PagamentoRepasse::getValor)
                .sum();
    }

    @Transient
    public double getSaldo() {
        double devido = valorDevido != null ? valorDevido : 0.0;
        double saldo = devido - getTotalPago();

        return Math.max(saldo, 0.0);
    }

    @Transient
    public String getStatus() {
        if (getTotalPago() <= 0) {
            return "PENDENTE";
        }

        if (getSaldo() > 0) {
            return "PARCIAL";
        }

        return "PAGO";
    }
}