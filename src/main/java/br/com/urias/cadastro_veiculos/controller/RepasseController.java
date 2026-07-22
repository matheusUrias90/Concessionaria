package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.ConfiguracaoConcessionaria;
import br.com.urias.cadastro_veiculos.model.PagamentoRepasse;
import br.com.urias.cadastro_veiculos.model.Repasse;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.repository.ConfiguracaoConcessionariaRepository;
import br.com.urias.cadastro_veiculos.repository.PagamentoRepasseRepository;
import br.com.urias.cadastro_veiculos.repository.RepasseRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RepasseController {

    private final RepasseRepository repasseRepository;
    private final VendaRepository vendaRepository;
    private final PagamentoRepasseRepository pagamentoRepasseRepository;
    private final ConfiguracaoConcessionariaRepository configuracaoRepository;

    public RepasseController(
            RepasseRepository repasseRepository,
            VendaRepository vendaRepository,
            PagamentoRepasseRepository pagamentoRepasseRepository,
            ConfiguracaoConcessionariaRepository configuracaoRepository) {

        this.repasseRepository = repasseRepository;
        this.vendaRepository = vendaRepository;
        this.pagamentoRepasseRepository = pagamentoRepasseRepository;
        this.configuracaoRepository = configuracaoRepository;
    }

    /*
     * =====================================================
     * LISTAGEM DE REPASSES
     * =====================================================
     */
    @GetMapping("/repasses")
    public String listar(
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/";
        }

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
            return "redirect:/dashboard-vendedor";
        }

        List<Repasse> repasses = repasseRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );

        List<Venda> vendas = vendaRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );

        /*
         * Guarda os IDs das vendas que já possuem repasse.
         */
        Set<Long> vendasComRepasse = repasses.stream()
                .filter(repasse -> repasse.getVenda() != null)
                .map(repasse -> repasse.getVenda().getId())
                .collect(Collectors.toSet());

        /*
         * Vendas que ainda não possuem um repasse iniciado.
         */
        List<Venda> vendasAguardandoRepasse = vendas.stream()
                .filter(venda ->
                        !vendasComRepasse.contains(venda.getId())
                )
                .toList();

        /*
         * Repasses com saldo pendente.
         * Inclui os status PENDENTE e PARCIAL.
         */
        List<Repasse> repassesPendentes = repasses.stream()
                .filter(repasse ->
                        !"PAGO".equals(repasse.getStatus())
                )
                .toList();

        /*
         * Repasses totalmente quitados.
         */
        List<Repasse> repassesFinalizados = repasses.stream()
                .filter(repasse ->
                        "PAGO".equals(repasse.getStatus())
                )
                .toList();

        model.addAttribute(
                "vendasAguardandoRepasse",
                vendasAguardandoRepasse
        );

        model.addAttribute(
                "repassesPendentes",
                repassesPendentes
        );

        model.addAttribute(
                "repassesFinalizados",
                repassesFinalizados
        );

        /*
         * Pode ser mantido caso alguma parte do HTML ainda utilize
         * a lista completa de repasses.
         */
        model.addAttribute("repasses", repasses);
        
         model.addAttribute("paginaAtiva", "repasses");

        return "repasses";
    }

    /*
     * =====================================================
     * INICIAR REPASSE
     * =====================================================
     */
    @GetMapping("/repasses/iniciar/{id}")
    public String iniciarRepasse(
            @PathVariable Long id,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/";
        }

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
            return "redirect:/dashboard-vendedor";
        }

        Venda venda = vendaRepository.findById(id).orElse(null);

        if (venda == null) {
            return "redirect:/repasses";
        }

        /*
         * Impede a criação de dois repasses para a mesma venda.
         */
        if (repasseRepository.findByVendaId(id).isPresent()) {
            return "redirect:/repasses";
        }

        Repasse repasse = new Repasse();

        repasse.setVenda(venda);

        /*
         * O proprietário recebe o preço original do veículo.
         * A comissão da concessionária está no valor final da venda.
         */
        repasse.setValorDevido(
                venda.getVeiculo().getPreco()
        );

        repasseRepository.save(repasse);

        return "redirect:/repasses";
    }

    /*
     * =====================================================
     * DETALHES DO REPASSE
     * =====================================================
     */
    @GetMapping("/repasses/{id}")
    public String abrirRepasse(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/";
        }

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
            return "redirect:/dashboard-vendedor";
        }

        Repasse repasse
                = repasseRepository.findById(id).orElse(null);

        if (repasse == null) {
            return "redirect:/repasses";
        }

        List<PagamentoRepasse> pagamentos
                = pagamentoRepasseRepository
                        .findByRepasseIdOrderByDataPagamentoDesc(id);

        model.addAttribute("repasse", repasse);
        model.addAttribute("pagamentos", pagamentos);

        return "repasse-detalhes";
    }

  
}