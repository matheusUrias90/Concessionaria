package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.dto.NegociacaoResumoDTO;
import br.com.urias.cadastro_veiculos.dto.PaginaNegociacaoDTO;
import br.com.urias.cadastro_veiculos.model.StatusNegociacao;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.NegociacaoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NegociacaoApiController {

    private final NegociacaoRepository negociacaoRepository;

    public NegociacaoApiController(
            NegociacaoRepository negociacaoRepository) {

        this.negociacaoRepository = negociacaoRepository;
    }

    @GetMapping("/api/negociacoes")
    public ResponseEntity<PaginaNegociacaoDTO> filtrarNegociacoes(
            @RequestParam(required = false) StatusNegociacao status,
            @RequestParam(required = false) String busca,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Long vendedorId = null;

        if ("VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil()))) {

            vendedorId = usuarioLogado.getId();
        }

        String textoBusca
                = busca == null ? "" : busca.trim();

        int paginaValida = Math.max(pagina, 0);
        int tamanhoValido = Math.min(Math.max(tamanho, 1), 50);

        Pageable pageable = PageRequest.of(
                paginaValida,
                tamanhoValido
        );

        Page<NegociacaoResumoDTO> resultado
                = negociacaoRepository
                        .filtrarNegociacoes(
                                status,
                                textoBusca,
                                vendedorId,
                                pageable
                        )
                        .map(NegociacaoResumoDTO::from);

        return ResponseEntity.ok(
                PaginaNegociacaoDTO.from(resultado)
        );
    }
}