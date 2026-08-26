package br.com.urias.cadastro_veiculos.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginaNegociacaoDTO(
        List<NegociacaoResumoDTO> negociacoes,
        int paginaAtual,
        int totalPaginas,
        long totalElementos,
        boolean primeira,
        boolean ultima
) {

    public static PaginaNegociacaoDTO from(
            Page<NegociacaoResumoDTO> pagina) {

        return new PaginaNegociacaoDTO(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getTotalPages(),
                pagina.getTotalElements(),
                pagina.isFirst(),
                pagina.isLast()
        );
    }
}