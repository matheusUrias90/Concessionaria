package br.com.urias.cadastro_veiculos.model;

public enum TipoFormaPagamento {

    DINHEIRO("Dinheiro"),
    PIX("PIX"),
    CARTAO("Cartão"),
    FINANCIAMENTO("Financiamento"),
    CONSORCIO("Consórcio"),
    VEICULO_ENTRADA("Veículo de entrada");

    private final String descricao;

    TipoFormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}