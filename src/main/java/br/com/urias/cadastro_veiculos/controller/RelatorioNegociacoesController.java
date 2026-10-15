package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Negociacao;
import br.com.urias.cadastro_veiculos.model.StatusNegociacao;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.NegociacaoRepository;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import br.com.urias.cadastro_veiculos.repository.ConfiguracaoConcessionariaRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

@Controller
@RequestMapping("/relatorios/negociacoes")
public class RelatorioNegociacoesController {

    private final NegociacaoRepository negociacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConfiguracaoConcessionariaRepository configuracaoRepository;

    public RelatorioNegociacoesController(
            NegociacaoRepository negociacaoRepository,
            UsuarioRepository usuarioRepository,
            ConfiguracaoConcessionariaRepository configuracaoRepository) {

        this.negociacaoRepository = negociacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.configuracaoRepository = configuracaoRepository;
    }

    @GetMapping
    public String relatorioNegociacoes(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) Long vendedorId,
            @RequestParam(defaultValue = "false") boolean imprimir,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (inicio != null && fim != null && inicio.isAfter(fim)) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "A data inicial não pode ser posterior à final."
            );

            return "redirect:/relatorios/negociacoes";
        }

        boolean perfilVendedor = "VENDEDOR".equals(usuario.getPerfil());

        // VENDEDOR consulta somente suas próprias negociações.
        if (perfilVendedor) {
            vendedorId = usuario.getId();
        }

        // A data final é inclusiva.
        List<Negociacao> negociacoes
                = negociacaoRepository.buscarNegociacoesRelatorio(
                        inicio != null ? inicio.atStartOfDay() : null,
                        fim != null ? fim.plusDays(1).atStartOfDay() : null,
                        vendedorId
                );

        long totalNegociacoes = negociacoes.size();

        long emAndamento = negociacoes.stream()
                .filter(n -> n.getStatus() == StatusNegociacao.EM_ANDAMENTO)
                .count();

        long concluidas = negociacoes.stream()
                .filter(n -> n.getStatus() == StatusNegociacao.CONCLUIDA)
                .count();

        long canceladas = negociacoes.stream()
                .filter(n -> n.getStatus() == StatusNegociacao.CANCELADA)
                .count();

        // Valor potencial das negociações em andamento.
        double valorPotencial = negociacoes.stream()
                .filter(n -> n.getStatus() == StatusNegociacao.EM_ANDAMENTO)
                .map(Negociacao::getValorProposto)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        // Apenas negociações encerradas entram na conversão.
        long encerradas = concluidas + canceladas;

        double taxaConversao = encerradas > 0
                ? (concluidas * 100.0) / encerradas
                : 0.0;

        // Evolução mensal pela data de início da negociação
        Map<YearMonth, Long> negociacoesMensais = new TreeMap<>();

        for (Negociacao negociacao : negociacoes) {

            if (negociacao.getDataInicio() != null) {

                YearMonth mes = YearMonth.from(negociacao.getDataInicio());

                negociacoesMensais.merge(mes, 1L, Long::sum);
            }
        }

        DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MM/yyyy");

        List<String> meses = new ArrayList<>();
        List<Long> quantidadesMensais = new ArrayList<>();

        negociacoesMensais.forEach((mes, quantidade) -> {
            meses.add(mes.format(formatoMes));
            quantidadesMensais.add(quantidade);
        });

        model.addAttribute("meses", meses);
        model.addAttribute("quantidadesMensais", quantidadesMensais);

        model.addAttribute("totalNegociacoes", totalNegociacoes);
        model.addAttribute("emAndamento", emAndamento);
        model.addAttribute("concluidas", concluidas);
        model.addAttribute("canceladas", canceladas);
        model.addAttribute("valorPotencial", valorPotencial);
        model.addAttribute("taxaConversao", taxaConversao);
        model.addAttribute("negociacoes", negociacoes);

        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);
        model.addAttribute("vendedorId", vendedorId);
        model.addAttribute("perfilVendedor", perfilVendedor);

        if (!perfilVendedor) {
            model.addAttribute(
                    "vendedores",
                    usuarioRepository.findByPerfilIn(
                            List.of("DONO", "GERENTE", "VENDEDOR")
                    )
            );
        }

        model.addAttribute("paginaAtiva", "relatorioNegociacoes");

        if (imprimir) {

            model.addAttribute(
                    "configuracao",
                    configuracaoRepository.findById(1L).orElse(null)
            );

            model.addAttribute("dataEmissao", LocalDate.now());

            model.addAttribute(
                    "nomeVendedor",
                    vendedorId != null
                            ? usuarioRepository.findById(vendedorId)
                                    .map(Usuario::getNome)
                                    .orElse("Não encontrado")
                            : "Todos"
            );

            return "relatorios/impressao-negociacoes";
        }

        return "relatorios/negociacoes";
    }
}
