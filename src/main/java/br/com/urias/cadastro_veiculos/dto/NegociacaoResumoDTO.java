package br.com.urias.cadastro_veiculos.dto;

import br.com.urias.cadastro_veiculos.model.Negociacao;
import java.time.format.DateTimeFormatter;

public record NegociacaoResumoDTO(
        Long id,
        String veiculo,
        String comprador,
        String vendedor,
        Double valorProposto,
        String status,
        String inicio
) {

    public static NegociacaoResumoDTO from(
            Negociacao negociacao) {

        return new NegociacaoResumoDTO(
                negociacao.getId(),
                negociacao.getVeiculo().getNome(),
                negociacao.getCliente().getNome(),
                negociacao.getVendedor().getNome(),
                negociacao.getValorProposto(),
                negociacao.getStatus().name(),
                negociacao.getDataInicio().format(
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy HH:mm"
                        )
                )
        );
    }
}