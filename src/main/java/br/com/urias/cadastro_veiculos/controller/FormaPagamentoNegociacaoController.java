package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.FormaPagamentoNegociacao;
import br.com.urias.cadastro_veiculos.model.StatusFormaPagamento;
import br.com.urias.cadastro_veiculos.model.StatusNegociacao;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.FormaPagamentoNegociacaoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormaPagamentoNegociacaoController {

    private final FormaPagamentoNegociacaoRepository formaPagamentoRepository;

    public FormaPagamentoNegociacaoController(
            FormaPagamentoNegociacaoRepository formaPagamentoRepository) {

        this.formaPagamentoRepository = formaPagamentoRepository;
    }

    @PostMapping("/negociacoes/formas-pagamento/{formaId}/confirmar")
    @Transactional
    public String confirmarFormaPagamento(
            @PathVariable Long formaId,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        FormaPagamentoNegociacao forma
                = buscarFormaComPermissao(
                        formaId,
                        usuarioLogado
                );

        if (forma.getNegociacao().getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return redirecionarParaNegociacao(forma);
        }

        forma.setStatus(StatusFormaPagamento.CONFIRMADA);
        formaPagamentoRepository.save(forma);

        return redirecionarParaNegociacao(forma);
    }

    @PostMapping("/negociacoes/formas-pagamento/{formaId}/recusar")
    @Transactional
    public String recusarFormaPagamento(
            @PathVariable Long formaId,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        FormaPagamentoNegociacao forma
                = buscarFormaComPermissao(
                        formaId,
                        usuarioLogado
                );

        if (forma.getNegociacao().getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return redirecionarParaNegociacao(forma);
        }

        forma.setStatus(StatusFormaPagamento.RECUSADA);
        formaPagamentoRepository.save(forma);

        return redirecionarParaNegociacao(forma);
    }

    private FormaPagamentoNegociacao buscarFormaComPermissao(
            Long formaId,
            Usuario usuarioLogado) {

        FormaPagamentoNegociacao forma
                = formaPagamentoRepository.findById(formaId)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Forma de pagamento não encontrada."
                        )
                        );

        boolean vendedor = "VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil())
        );

        boolean negociacaoDeOutroVendedor
                = !forma.getNegociacao()
                        .getVendedor()
                        .getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            throw new IllegalStateException(
                    "Você não possui permissão para alterar esta negociação."
            );
        }

        return forma;
    }

    private String redirecionarParaNegociacao(
            FormaPagamentoNegociacao forma) {

        return "redirect:/negociacoes/"
                + forma.getNegociacao().getId();
    }
}