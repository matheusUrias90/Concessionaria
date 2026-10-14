
package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.PagamentoRepasse;
import br.com.urias.cadastro_veiculos.model.Repasse;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.repository.PagamentoRepasseRepository;
import br.com.urias.cadastro_veiculos.repository.RepasseRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/relatorios/repasses")
public class RelatorioRepassesController {

    private final RepasseRepository repasseRepository;
    private final PagamentoRepasseRepository pagamentoRepasseRepository;
    private final VendaRepository vendaRepository;

    public RelatorioRepassesController(
            RepasseRepository repasseRepository,
            PagamentoRepasseRepository pagamentoRepasseRepository,
            VendaRepository vendaRepository) {

        this.repasseRepository = repasseRepository;
        this.pagamentoRepasseRepository = pagamentoRepasseRepository;
        this.vendaRepository = vendaRepository;
    }

    @GetMapping
    public String relatorio(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim,

            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!"DONO".equals(usuario.getPerfil())
                && !"GERENTE".equals(usuario.getPerfil())) {

            return "redirect:/dashboard-vendedor";
        }

        if (inicio != null && fim != null && inicio.isAfter(fim)) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "A data inicial não pode ser posterior à final."
            );

            return "redirect:/relatorios/repasses";
        }

        List<Repasse> repasses = repasseRepository.findAll();
        List<Venda> vendas = vendaRepository.findAll();

        // Indexa os repasses pela venda.
        Map<Long, Repasse> repassePorVenda = new HashMap<>();

        for (Repasse repasse : repasses) {
            repassePorVenda.put(repasse.getVenda().getId(), repasse);
        }

        // Indexa os pagamentos por repasse.
        Map<Long, Double> totalPagoPorRepasse = new HashMap<>();

        List<PagamentoRepasse> pagamentos =
                pagamentoRepasseRepository.findAll();

        for (PagamentoRepasse pagamento : pagamentos) {

            Long repasseId = pagamento.getRepasse().getId();

            totalPagoPorRepasse.merge(
                    repasseId,
                    pagamento.getValor(),
                    Double::sum
            );
        }

        double totalDevido = 0.0;
        double totalPago = 0.0;
        double saldoPendente = 0.0;

        long pendentes = 0;
        long parciais = 0;
        long quitados = 0;

        // Considera também vendas que ainda não tiveram
        // um repasse iniciado.
        for (Venda venda : vendas) {

            LocalDate dataVenda = venda.getDataVenda();

            if (dataVenda == null) {
                continue;
            }

            if (inicio != null && dataVenda.isBefore(inicio)) {
                continue;
            }

            if (fim != null && dataVenda.isAfter(fim)) {
                continue;
            }

            Repasse repasse = repassePorVenda.get(venda.getId());

            double valorDevido;
            double valorPago;

            if (repasse != null) {

                valorDevido = repasse.getValorDevido();

                valorPago = totalPagoPorRepasse.getOrDefault(
                        repasse.getId(),
                        0.0
                );

            } else {

                // Venda ainda aguardando início do repasse.
                // A comissão de 8% está incluída no valor da venda.
                valorDevido = venda.getVeiculo().getPreco();
                valorPago = 0.0;
            }

            double saldo = Math.max(0.0, valorDevido - valorPago);

            totalDevido += valorDevido;
            totalPago += valorPago;
            saldoPendente += saldo;

            if (saldo <= 0.001) {
                quitados++;
            } else if (valorPago > 0.001) {
                parciais++;
            } else {
                pendentes++;
            }
        }

        // Gráfico mensal: utiliza a data de cada pagamento,
        // independentemente da data da venda.
        Map<YearMonth, Double> pagamentosMensais = new TreeMap<>();

        for (PagamentoRepasse pagamento : pagamentos) {

            LocalDate data = pagamento.getDataPagamento();

            if (data == null) {
                continue;
            }

            if (inicio != null && data.isBefore(inicio)) {
                continue;
            }

            if (fim != null && data.isAfter(fim)) {
                continue;
            }

            YearMonth mes = YearMonth.from(data);

            pagamentosMensais.merge(
                    mes,
                    pagamento.getValor(),
                    Double::sum
            );
        }

        DateTimeFormatter formatoMes =
                DateTimeFormatter.ofPattern("MM/yyyy");

        List<String> meses = new ArrayList<>();
        List<Double> valoresMensais = new ArrayList<>();

        pagamentosMensais.forEach((mes, valor) -> {

            meses.add(mes.format(formatoMes));
            valoresMensais.add(valor);
        });

        model.addAttribute("totalDevido", totalDevido);
        model.addAttribute("totalPago", totalPago);
        model.addAttribute("saldoPendente", saldoPendente);

        model.addAttribute("pendentes", pendentes);
        model.addAttribute("parciais", parciais);
        model.addAttribute("quitados", quitados);

        model.addAttribute("meses", meses);
        model.addAttribute("valoresMensais", valoresMensais);

        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);

        model.addAttribute("paginaAtiva", "relatorioRepasses");

        return "relatorios/repasses";
    }
}
