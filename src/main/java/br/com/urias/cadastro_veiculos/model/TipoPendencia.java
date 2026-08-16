package br.com.urias.cadastro_veiculos.model;

public enum TipoPendencia {

    FINANCIAMENTO("Aprovação do financiamento"),
    CONSORCIO("Liberação da carta de crédito"),
    VEICULO_ENTRADA("Avaliação do veículo de entrada"),
    DEVOLUCAO_CLIENTE("Devolução ao comprador");

    private final String descricao;

    TipoPendencia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}