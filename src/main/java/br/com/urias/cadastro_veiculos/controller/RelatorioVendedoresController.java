package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Negociacao;
import br.com.urias.cadastro_veiculos.model.StatusNegociacao;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.repository.NegociacaoRepository;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import br.com.urias.cadastro_veiculos.repository.ConfiguracaoConcessionariaRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Controller
@RequestMapping("/relatorios/vendedores")
public class RelatorioVendedoresController {

    private final VendaRepository vendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final NegociacaoRepository negociacaoRepository;
    private final ConfiguracaoConcessionariaRepository configuracaoRepository;

    public RelatorioVendedoresController(
            VendaRepository vendaRepository,
            UsuarioRepository usuarioRepository,
            NegociacaoRepository negociacaoRepository,
            ConfiguracaoConcessionariaRepository configuracaoRepository) {

        this.vendaRepository = vendaRepository;
        this.usuarioRepository = usuarioRepository;
        this.negociacaoRepository = negociacaoRepository;
        this.configuracaoRepository = configuracaoRepository;
    }

    @GetMapping
    public String relatorioVendedores(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) Long vendedorId,
            @RequestParam(defaultValue = "false") boolean imprimir,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(defaultValue = "0") int pagina) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (inicio != null && fim != null && inicio.isAfter(fim)) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "A data inicial não pode ser posterior à final."
            );

            return "redirect:/relatorios/vendedores";
        }

        boolean perfilVendedor = "VENDEDOR".equals(usuario.getPerfil());

        // Vendedor só visualiza seus próprios resultados.
        if (perfilVendedor) {
            vendedorId = usuario.getId();
        }

        // Todos os usuários que podem realizar vendas.
        List<Usuario> usuarios = usuarioRepository.findByPerfilIn(
                List.of("DONO", "GERENTE", "VENDEDOR")
        );

        if (perfilVendedor) {

            usuarios = usuarios.stream()
                    .filter(u -> u.getId().equals(usuario.getId()))
                    .toList();

        } else if (vendedorId != null) {

            Long idFiltro = vendedorId;

            usuarios = usuarios.stream()
                    .filter(u -> u.getId().equals(idFiltro))
                    .toList();
        }

        // Vendas no período.
        List<Venda> vendas = vendaRepository.buscarVendasRelatorio(
                inicio,
                fim,
                vendedorId
        );

        // Negociações no período.
        // A data final é inclusiva.
        List<Negociacao> negociacoes
                = negociacaoRepository.buscarNegociacoesRelatorio(
                        inicio != null ? inicio.atStartOfDay() : null,
                        fim != null ? fim.plusDays(1).atStartOfDay() : null,
                        vendedorId
                );

        // Contadores das vendas.
        Map<Long, Long> quantidadePorUsuario = new HashMap<>();
        Map<Long, Double> valorPorUsuario = new HashMap<>();

        // Contadores das negociações.
        Map<Long, Long> negociacoesPorUsuario = new HashMap<>();
        Map<Long, Long> andamentoPorUsuario = new HashMap<>();
        Map<Long, Long> concluidasPorUsuario = new HashMap<>();
        Map<Long, Long> canceladasPorUsuario = new HashMap<>();

        // Dados mensais dos gráficos.
        Map<YearMonth, Double> valoresMensais = new TreeMap<>();
        Map<YearMonth, Long> quantidadesMensais = new TreeMap<>();

        // Processamento das vendas.
        for (Venda venda : vendas) {

            if (venda.getVendedor() == null) {
                continue;
            }

            Long idUsuario = venda.getVendedor().getId();

            quantidadePorUsuario.merge(
                    idUsuario,
                    1L,
                    Long::sum
            );

            double valor = venda.getValor() != null
                    ? venda.getValor()
                    : 0.0;

            valorPorUsuario.merge(
                    idUsuario,
                    valor,
                    Double::sum
            );

            if (venda.getDataVenda() != null) {

                YearMonth mes = YearMonth.from(venda.getDataVenda());

                valoresMensais.merge(
                        mes,
                        valor,
                        Double::sum
                );

                quantidadesMensais.merge(
                        mes,
                        1L,
                        Long::sum
                );
            }
        }

        // Processamento das negociações.
        for (Negociacao negociacao : negociacoes) {

            if (negociacao.getVendedor() == null) {
                continue;
            }

            Long idUsuario = negociacao.getVendedor().getId();

            negociacoesPorUsuario.merge(
                    idUsuario,
                    1L,
                    Long::sum
            );

            if (negociacao.getStatus() == StatusNegociacao.EM_ANDAMENTO) {

                andamentoPorUsuario.merge(
                        idUsuario,
                        1L,
                        Long::sum
                );

            } else if (negociacao.getStatus() == StatusNegociacao.CONCLUIDA) {

                concluidasPorUsuario.merge(
                        idUsuario,
                        1L,
                        Long::sum
                );

            } else if (negociacao.getStatus() == StatusNegociacao.CANCELADA) {

                canceladasPorUsuario.merge(
                        idUsuario,
                        1L,
                        Long::sum
                );
            }
        }

        // Montagem da tabela de desempenho.
        List<Map<String, Object>> desempenho = new ArrayList<>();

        for (Usuario u : usuarios) {

            Long id = u.getId();

            long quantidade = quantidadePorUsuario.getOrDefault(id, 0L);
            double valor = valorPorUsuario.getOrDefault(id, 0.0);

            double ticketMedio = quantidade > 0
                    ? valor / quantidade
                    : 0.0;

            Map<String, Object> linha = new HashMap<>();

            linha.put("nome", u.getNome());
            linha.put("perfil", u.getPerfil());

            linha.put("negociacoes",
                    negociacoesPorUsuario.getOrDefault(id, 0L));

            linha.put("emAndamento",
                    andamentoPorUsuario.getOrDefault(id, 0L));

            linha.put("concluidas",
                    concluidasPorUsuario.getOrDefault(id, 0L));

            linha.put("canceladas",
                    canceladasPorUsuario.getOrDefault(id, 0L));

            linha.put("quantidade", quantidade);
            linha.put("valor", valor);
            linha.put("ticketMedio", ticketMedio);

            desempenho.add(linha);
        }

        // Ordenação apenas para organização da tabela.
        // Maior valor vendido primeiro.
        desempenho.sort((a, b) -> {

            int comparacao = Double.compare(
                    (Double) b.get("valor"),
                    (Double) a.get("valor")
            );

            if (comparacao != 0) {
                return comparacao;
            }

            return ((String) a.get("nome"))
                    .compareToIgnoreCase((String) b.get("nome"));
        });
        
        // Paginação da tabela de desempenho
        int tamanhoPagina = 10;

        int totalRegistros = desempenho.size();
        int totalPaginas = (int) Math.ceil(
                (double) totalRegistros / tamanhoPagina
        );

        pagina = Math.max(0, pagina);

        if (totalPaginas > 0) {
            pagina = Math.min(pagina, totalPaginas - 1);
        } else {
            pagina = 0;
        }

        int inicioPagina = Math.min(
                pagina * tamanhoPagina,
                totalRegistros
        );

        int fimPagina = Math.min(
                inicioPagina + tamanhoPagina,
                totalRegistros
        );

        List<Map<String, Object>> desempenhoPaginado
                = desempenho.subList(inicioPagina, fimPagina);

        // Totais das vendas.
        long totalVendas = vendas.size();

        double valorTotal = vendas.stream()
                .map(Venda::getValor)
                .filter(valor -> valor != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        double ticketMedioGeral = totalVendas > 0
                ? valorTotal / totalVendas
                : 0.0;

        // Preparação dos gráficos.
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

        // Dados enviados ao Thymeleaf.
        model.addAttribute(
                "desempenho",
                imprimir ? desempenho : desempenhoPaginado
        );

        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("totalPaginas", totalPaginas);

        model.addAttribute("totalVendas", totalVendas);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("ticketMedio", ticketMedioGeral);

        model.addAttribute("meses", meses);
        model.addAttribute("valoresMensais", valores);
        model.addAttribute("quantidadesMensais", quantidades);

        if (!perfilVendedor) {

            model.addAttribute(
                    "usuariosFiltro",
                    usuarioRepository.findByPerfilIn(
                            List.of("DONO", "GERENTE", "VENDEDOR")
                    )
            );
        }

        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);
        model.addAttribute("vendedorId", vendedorId);
        model.addAttribute("perfilVendedor", perfilVendedor);

        model.addAttribute("paginaAtiva", "relatorioVendedores");

        if (imprimir) {

            model.addAttribute(
                    "configuracao",
                    configuracaoRepository.findById(1L).orElse(null)
            );

            model.addAttribute("dataEmissao", LocalDate.now());

            return "relatorios/impressao-vendedores";
        }

        return "relatorios/vendedores";
    }
}
