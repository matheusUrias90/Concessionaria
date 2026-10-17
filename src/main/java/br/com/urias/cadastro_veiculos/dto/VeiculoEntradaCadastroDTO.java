package br.com.urias.cadastro_veiculos.dto;

import java.util.ArrayList;
import java.util.List;

public class VeiculoEntradaCadastroDTO {

    private Long formaPagamentoId;
    private Double precoVenda;
    private List<String> itensChecklist = new ArrayList<>();

    public Long getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(Long formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }

    public Double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(Double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public List<String> getItensChecklist() {
        return itensChecklist;
    }

    public void setItensChecklist(List<String> itensChecklist) {
        this.itensChecklist = itensChecklist;
    }
}
