package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Cliente;
import br.com.urias.cadastro_veiculos.model.FormaPagamentoNegociacao;
import br.com.urias.cadastro_veiculos.model.Negociacao;
import br.com.urias.cadastro_veiculos.model.StatusFormaPagamento;
import br.com.urias.cadastro_veiculos.model.StatusNegociacao;
import br.com.urias.cadastro_veiculos.model.StatusVeiculo;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Veiculo;
import br.com.urias.cadastro_veiculos.repository.ClienteRepository;
import br.com.urias.cadastro_veiculos.repository.NegociacaoRepository;
import br.com.urias.cadastro_veiculos.repository.VeiculoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NegociacaoController {

    private final NegociacaoRepository negociacaoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;

    public NegociacaoController(
            NegociacaoRepository negociacaoRepository,
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository) {

        this.negociacaoRepository = negociacaoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping("/negociacoes/nova")
    public String novaNegociacao(Model model, HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "clientes",
                clienteRepository.findAll()
        );

        model.addAttribute(
                "veiculos",
                veiculoRepository.findByStatus(
                        StatusVeiculo.DISPONIVEL
                )
        );

        model.addAttribute("paginaAtiva", "negociacoes");

        return "negociacao-form";
    }

    @PostMapping("/negociacoes/iniciar")
    @Transactional
    public String iniciarNegociacao(
            @RequestParam Long clienteId,
            @RequestParam Long veiculoId,
            @ModelAttribute Negociacao negociacao,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Comprador não encontrado."
                        )
                );

        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Veículo não encontrado."
                        )
                );

        if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL) {
            throw new IllegalStateException(
                    "Este veículo não está mais disponível."
            );
        }

        negociacao.setCliente(cliente);
        negociacao.setVeiculo(veiculo);
        negociacao.setVendedor(usuarioLogado);
        negociacao.setDataInicio(LocalDateTime.now());
        negociacao.setStatus(StatusNegociacao.EM_ANDAMENTO);

        for (FormaPagamentoNegociacao forma
                : negociacao.getFormasPagamento()) {

            forma.setStatus(StatusFormaPagamento.PENDENTE);
            forma.setNegociacao(negociacao);
        }

        veiculo.setStatus(StatusVeiculo.RESERVADO);

        negociacaoRepository.save(negociacao);
        veiculoRepository.save(veiculo);

        return "redirect:/negociacoes";
    }

    @GetMapping("/negociacoes")
    public String listarNegociacoes(
            Model model,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "negociacoes",
                negociacaoRepository
                        .findByStatusOrderByDataInicioDesc(
                                StatusNegociacao.EM_ANDAMENTO
                        )
        );

        model.addAttribute("paginaAtiva", "negociacoes");

        return "negociacoes";
    }
}