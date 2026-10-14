package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.StatusVeiculo;
import br.com.urias.cadastro_veiculos.model.Veiculo;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.VeiculoRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;

@Controller
@RequestMapping("/relatorios/estoque")
public class RelatorioEstoqueController {

    private final VeiculoRepository veiculoRepository;
    private final VendaRepository vendaRepository;

    public RelatorioEstoqueController(
            VeiculoRepository veiculoRepository,
            VendaRepository vendaRepository) {

        this.veiculoRepository = veiculoRepository;
        this.vendaRepository = vendaRepository;
    }

    @GetMapping
    public String relatorioEstoque(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim,
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int pagina,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!usuario.getPerfil().equals("DONO")
                && !usuario.getPerfil().equals("GERENTE")) {
            return "redirect:/dashboard-vendedor";
        }

        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            redirectAttributes.addFlashAttribute(
                    "erro", "A data inicial não pode ser posterior à final."
            );
            return "redirect:/relatorios/estoque";
        }

        // INDICADORES DO ESTOQUE ATUAL
        long disponiveis = veiculoRepository.countByStatus(
                StatusVeiculo.DISPONIVEL
        );

        long reservados = veiculoRepository.countByStatus(
                StatusVeiculo.RESERVADO
        );

        long totalEstoque = disponiveis + reservados;

        Double valorEstoque = veiculoRepository.somarValorEstoque(
                List.of(
                        StatusVeiculo.DISPONIVEL,
                        StatusVeiculo.RESERVADO
                )
        );

        // VEÍCULOS EM ESTOQUE
        Pageable pageable = PageRequest.of(
                Math.max(0, pagina),
                10,
                Sort.by(
                        Sort.Order.asc("dataEntrada").nullsLast(),
                        Sort.Order.asc("id")
                )
        );

        Page<Veiculo> paginaVeiculos
                = veiculoRepository.buscarEstoqueRelatorio(
                        busca.trim(),
                        pageable
                );

        List<Veiculo> veiculos = paginaVeiculos.getContent();

        Map<Long, Long> diasEstoque = new java.util.HashMap<>();

        for (Veiculo veiculo : veiculos) {
            if (veiculo.getDataEntrada() != null) {
                diasEstoque.put(
                        veiculo.getId(),
                        Math.max(0, ChronoUnit.DAYS.between(
                                veiculo.getDataEntrada(),
                                LocalDate.now()
                        ))
                );
            }
        }

        // ANÁLISE DAS VENDAS NO PERÍODO
        List<Venda> vendas = vendaRepository.buscarVendasPorPeriodo(
                inicio, fim
        );

        double precoMedioVenda = vendas.stream()
                .map(Venda::getValor)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double tempoMedioVenda = vendas.stream()
                .filter(v -> v.getDataVenda() != null)
                .filter(v -> v.getVeiculo() != null)
                .filter(v -> v.getVeiculo().getDataEntrada() != null)
                .mapToLong(v -> Math.max(0, ChronoUnit.DAYS.between(
                v.getVeiculo().getDataEntrada(),
                v.getDataVenda()
        )))
                .average()
                .orElse(0.0);

        // FAIXAS DE PREÇO: INTERVALOS DE R$ 20.000
        Map<String, Long> faixasPreco = vendas.stream()
                .map(Venda::getValor)
                .filter(Objects::nonNull)
                .filter(valor -> valor >= 0)
                .collect(Collectors.groupingBy(
                        valor -> {
                            long faixa = (long) (valor / 20000);
                            long minimo = faixa * 20000;
                            long maximo = minimo + 19999;
                            return String.format(
                                    "R$ %,d a R$ %,d",
                                    minimo, maximo
                            );
                        },
                        TreeMap::new,
                        Collectors.counting()
                ));

        model.addAttribute("disponiveis", disponiveis);
        model.addAttribute("reservados", reservados);
        model.addAttribute("totalEstoque", totalEstoque);
        model.addAttribute("valorEstoque", valorEstoque);

        model.addAttribute("veiculos", veiculos);
        model.addAttribute("paginaAtual", paginaVeiculos.getNumber());
        model.addAttribute("totalPaginas", paginaVeiculos.getTotalPages());
        model.addAttribute("totalResultados", paginaVeiculos.getTotalElements());
        model.addAttribute("busca", busca);
        model.addAttribute("diasEstoque", diasEstoque);

        model.addAttribute("precoMedioVenda", precoMedioVenda);
        model.addAttribute("tempoMedioVenda", tempoMedioVenda);
        model.addAttribute("faixasPreco", faixasPreco);

        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);

        model.addAttribute("paginaAtiva", "relatorioEstoque");

        return "relatorios/estoque";
    }

    @GetMapping("/buscar")
    @ResponseBody
    public Map<String, Object> buscarVeiculos(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int pagina,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null
                || (!usuario.getPerfil().equals("DONO")
                && !usuario.getPerfil().equals("GERENTE"))) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN
            );
        }

        Pageable pageable = PageRequest.of(
                Math.max(0, pagina),
                10,
                Sort.by(
                        Sort.Order.asc("dataEntrada").nullsLast(),
                        Sort.Order.asc("id")
                )
        );

        Page<Veiculo> resultado = veiculoRepository.buscarEstoqueRelatorio(
                busca.trim(), pageable
        );

        List<Map<String, Object>> veiculos = new ArrayList<>();

        for (Veiculo v : resultado.getContent()) {

            Map<String, Object> item = new HashMap<>();

            item.put("nome", v.getNome());
            item.put("ano", v.getAno());
            item.put("placa", v.getPlaca());
            item.put("status", v.getStatus().name());

            item.put("dataEntrada", v.getDataEntrada() == null
                    ? null : v.getDataEntrada().toString());

            item.put("diasEstoque", v.getDataEntrada() == null
                    ? null : Math.max(0, ChronoUnit.DAYS.between(
                            v.getDataEntrada(), LocalDate.now()
                    )));

            veiculos.add(item);
        }

        Map<String, Object> resposta = new HashMap<>();

        resposta.put("veiculos", veiculos);
        resposta.put("paginaAtual", resultado.getNumber());
        resposta.put("totalPaginas", resultado.getTotalPages());
        resposta.put("totalResultados", resultado.getTotalElements());

        return resposta;
    }
}
