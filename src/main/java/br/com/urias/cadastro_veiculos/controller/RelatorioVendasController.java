package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.urias.cadastro_veiculos.repository.ConfiguracaoConcessionariaRepository;

@Controller
@RequestMapping("/relatorios/vendas")
public class RelatorioVendasController {

    private final VendaRepository vendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConfiguracaoConcessionariaRepository configuracaoRepository;

    public RelatorioVendasController(
            VendaRepository vendaRepository,
            UsuarioRepository usuarioRepository,
            ConfiguracaoConcessionariaRepository configuracaoRepository) {

        this.vendaRepository = vendaRepository;
        this.usuarioRepository = usuarioRepository;
        this.configuracaoRepository = configuracaoRepository;
    }

    @GetMapping
    public String relatorioVendas(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) Long vendedorId,
            @RequestParam(defaultValue = "0") int pagina,
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

            return "redirect:/relatorios/vendas";
        }

        boolean vendedor = "VENDEDOR".equals(usuario.getPerfil());

        // O vendedor só pode consultar suas próprias vendas.
        if (vendedor) {
            vendedorId = usuario.getId();
        }

        List<Venda> vendas = vendaRepository.buscarVendasRelatorio(
                inicio,
                fim,
                vendedorId
        );

        long totalVendas = vendas.size();

        double valorTotal = vendas.stream()
                .map(Venda::getValor)
                .filter(valor -> valor != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        double ticketMedio = totalVendas > 0
                ? valorTotal / totalVendas
                : 0;

        // Agrupamento mensal para os dois gráficos.
        Map<YearMonth, Double> valoresMensais = new LinkedHashMap<>();
        Map<YearMonth, Long> quantidadesMensais = new LinkedHashMap<>();

        for (Venda venda : vendas) {

            if (venda.getDataVenda() == null) {
                continue;
            }

            YearMonth mes = YearMonth.from(venda.getDataVenda());

            valoresMensais.merge(
                    mes,
                    venda.getValor() != null ? venda.getValor() : 0.0,
                    Double::sum
            );

            quantidadesMensais.merge(mes, 1L, Long::sum);
        }

        DateTimeFormatter formatoMes
                = DateTimeFormatter.ofPattern("MM/yyyy");

        List<String> meses = new ArrayList<>();
        List<Double> valores = new ArrayList<>();
        List<Long> quantidades = new ArrayList<>();

        for (YearMonth mes : valoresMensais.keySet()) {
            meses.add(mes.format(formatoMes));
            valores.add(valoresMensais.get(mes));
            quantidades.add(quantidadesMensais.get(mes));
        }

        // Paginação da tabela de vendas
        int tamanhoPagina = 10;
        int totalPaginas = (int) Math.ceil(
                (double) vendas.size() / tamanhoPagina
        );

        int paginaAtual = Math.max(0, Math.min(pagina, Math.max(0, totalPaginas - 1)));

        int inicioPagina = paginaAtual * tamanhoPagina;
        int fimPagina = Math.min(inicioPagina + tamanhoPagina, vendas.size());

        model.addAttribute("vendasPagina", vendas.subList(inicioPagina, fimPagina));
        model.addAttribute("paginaAtual", paginaAtual);
        model.addAttribute("totalPaginas", totalPaginas);

        model.addAttribute("totalVendas", totalVendas);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("ticketMedio", ticketMedio);

        model.addAttribute("meses", meses);
        model.addAttribute("valoresMensais", valores);
        model.addAttribute("quantidadesMensais", quantidades);

        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);
        model.addAttribute("vendedorId", vendedorId);
        model.addAttribute("perfilVendedor", vendedor);

        if (!vendedor) {
            model.addAttribute(
                    "vendedores",
                    usuarioRepository.findByPerfilIn(
                            List.of("DONO", "GERENTE", "VENDEDOR")
                    )
            );
        }

        model.addAttribute("paginaAtiva", "relatorioVendas");

        return "relatorios/vendas";
    }

    @GetMapping("/imprimir")
    public String imprimirRelatorio(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) Long vendedorId,
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

            return "redirect:/relatorios/vendas";
        }

        boolean vendedor = "VENDEDOR".equals(usuario.getPerfil());

        // O vendedor só pode imprimir suas próprias vendas.
        if (vendedor) {
            vendedorId = usuario.getId();
        }

        // Busca todas as vendas filtradas, sem paginação.
        List<Venda> vendas = vendaRepository.buscarVendasRelatorio(
                inicio,
                fim,
                vendedorId
        );

        long totalVendas = vendas.size();

        double valorTotal = vendas.stream()
                .map(Venda::getValor)
                .filter(valor -> valor != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        double ticketMedio = totalVendas > 0
                ? valorTotal / totalVendas
                : 0;

        model.addAttribute("configuracao",
                configuracaoRepository.findById(1L).orElse(null));

        model.addAttribute("vendas", vendas);
        model.addAttribute("totalVendas", totalVendas);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("ticketMedio", ticketMedio);

        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);
        model.addAttribute("vendedorId", vendedorId);

        // Nome do vendedor selecionado, quando houver.
        if (vendedorId != null) {
            usuarioRepository.findById(vendedorId)
                    .ifPresent(v -> model.addAttribute(
                    "nomeVendedor", v.getNome()
            ));
        }

        model.addAttribute("dataEmissao", LocalDate.now());

        return "relatorios/impressao-vendas";
    }
}
