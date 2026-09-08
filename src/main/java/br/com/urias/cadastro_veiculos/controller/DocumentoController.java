package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.ConfiguracaoConcessionaria;
import br.com.urias.cadastro_veiculos.model.PagamentoRepasse;
import br.com.urias.cadastro_veiculos.model.Repasse;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Veiculo;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.model.FormaPagamentoNegociacao;
import br.com.urias.cadastro_veiculos.repository.ConfiguracaoConcessionariaRepository;
import br.com.urias.cadastro_veiculos.repository.PagamentoRepasseRepository;
import br.com.urias.cadastro_veiculos.repository.RepasseRepository;
import br.com.urias.cadastro_veiculos.repository.VeiculoRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DocumentoController {

    private final VendaRepository vendaRepository;
    private final VeiculoRepository veiculoRepository;
    private final ConfiguracaoConcessionariaRepository configuracaoRepository;
    private final RepasseRepository repasseRepository;
    private final PagamentoRepasseRepository pagamentoRepasseRepository;

    public DocumentoController(
            VendaRepository vendaRepository,
            VeiculoRepository veiculoRepository,
            ConfiguracaoConcessionariaRepository configuracaoRepository,
            RepasseRepository repasseRepository,
            PagamentoRepasseRepository pagamentoRepasseRepository) {

        this.vendaRepository = vendaRepository;
        this.veiculoRepository = veiculoRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.repasseRepository = repasseRepository;
        this.pagamentoRepasseRepository = pagamentoRepasseRepository;
    }

    @GetMapping("/documentos/comprovante-venda/{id}")
    public String comprovanteVenda(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Venda venda = vendaRepository.findById(id).orElse(null);

        if (venda == null) {

            if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
                return "redirect:/dashboard-vendedor";
            }

            return "redirect:/admin/vendas";
        }

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {

            if (venda.getVendedor() == null
                    || !venda.getVendedor().getId()
                            .equals(usuarioLogado.getId())) {

                return "redirect:/dashboard-vendedor";
            }
        }

        ConfiguracaoConcessionaria configuracao
                = configuracaoRepository.findById(1L).orElse(null);
        
        List<FormaPagamentoNegociacao> formasPagamento = List.of();

        if (venda.getNegociacao() != null
                && venda.getNegociacao().getFormasPagamento() != null) {

            formasPagamento
                    = venda.getNegociacao().getFormasPagamento();
        }

        model.addAttribute("venda", venda);
        model.addAttribute("cliente", venda.getCliente());
        model.addAttribute("veiculo", venda.getVeiculo());
        model.addAttribute("pagamento", venda.getPagamento());
        model.addAttribute("formasPagamento",formasPagamento);
        model.addAttribute("vendedor", venda.getVendedor());
        model.addAttribute("configuracao", configuracao);

        return "documentos/comprovante-venda";
    }
    
    @GetMapping("/documentos/contrato-venda/{id}")
    public String contratoVenda(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Venda venda = vendaRepository.findById(id).orElse(null);

        if (venda == null) {

            if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
                return "redirect:/dashboard-vendedor";
            }

            return "redirect:/admin/vendas";
        }

        /*
     * O vendedor só pode acessar documentos
     * relacionados às próprias vendas.
         */
        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {

            if (venda.getVendedor() == null
                    || !venda.getVendedor().getId()
                            .equals(usuarioLogado.getId())) {

                return "redirect:/dashboard-vendedor";
            }
        }

        ConfiguracaoConcessionaria configuracao
                = configuracaoRepository.findById(1L).orElse(null);

        List<FormaPagamentoNegociacao> formasPagamento
                = List.of();

        /*
     * Vendas concluídas pelo novo fluxo possuem
     * múltiplas formas de pagamento.
         */
        if (venda.getNegociacao() != null
                && venda.getNegociacao().getFormasPagamento() != null) {

            formasPagamento
                    = venda.getNegociacao().getFormasPagamento();
        }
        
        double totalFormasPagamento = 0.0;

        for (FormaPagamentoNegociacao forma : formasPagamento) {

            if (forma.getValor() != null) {
                totalFormasPagamento += forma.getValor();
            }
        }

        boolean pagamentoTotalizado
                = Math.abs(totalFormasPagamento - venda.getValor()) < 0.01;

        model.addAttribute("venda", venda);
        model.addAttribute("cliente", venda.getCliente());
        model.addAttribute("veiculo", venda.getVeiculo());
        model.addAttribute("pagamento", venda.getPagamento());
        model.addAttribute("formasPagamento", formasPagamento);
        model.addAttribute("vendedor", venda.getVendedor());
        model.addAttribute("configuracao", configuracao);
        model.addAttribute("totalFormasPagamento",totalFormasPagamento);
        model.addAttribute("pagamentoTotalizado",pagamentoTotalizado);

        return "documentos/contrato-venda";
    }

    @GetMapping("/documentos/termo-veiculos-entrada/{id}")
    public String termoVeiculosEntrada(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Venda venda = vendaRepository.findById(id).orElse(null);

        if (venda == null) {

            if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
                return "redirect:/dashboard-vendedor";
            }

            return "redirect:/admin/vendas";
        }

        /*
     * O vendedor só pode acessar documentos
     * das próprias vendas.
         */
        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {

            if (venda.getVendedor() == null
                    || !venda.getVendedor().getId()
                            .equals(usuarioLogado.getId())) {

                return "redirect:/dashboard-vendedor";
            }
        }

        /*
     * Vendas do fluxo antigo não possuem negociação.
         */
        if (venda.getNegociacao() == null) {
            return "redirect:/documentos/comprovante-venda/" + id;
        }

        List<FormaPagamentoNegociacao> formasVeiculoEntrada
                = new ArrayList<>();

        /*
     * Separa somente as formas que possuem
     * um veículo recebido como entrada.
         */
        for (FormaPagamentoNegociacao forma
                : venda.getNegociacao().getFormasPagamento()) {

            if (forma.getVeiculoEntrada() != null) {
                formasVeiculoEntrada.add(forma);
            }
        }

        /*
     * Não existe termo de entrada se nenhum
     * veículo tiver sido recebido.
         */
        if (formasVeiculoEntrada.isEmpty()) {
            return "redirect:/documentos/comprovante-venda/" + id;
        }

        ConfiguracaoConcessionaria configuracao
                = configuracaoRepository.findById(1L).orElse(null);

        model.addAttribute("venda", venda);
        model.addAttribute("cliente", venda.getCliente());
        model.addAttribute("negociacao", venda.getNegociacao());
        model.addAttribute(
                "formasVeiculoEntrada",
                formasVeiculoEntrada
        );
        model.addAttribute("configuracao", configuracao);

        return "documentos/termo-veiculos-entrada";
    }

    @GetMapping("/documentos/contrato-consignacao/{id}")
    public String contratoConsignacao(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Veiculo veiculo = veiculoRepository.findById(id).orElse(null);

        if (veiculo == null) {
            return "redirect:/veiculos";
        }

        ConfiguracaoConcessionaria configuracao
                = configuracaoRepository.findById(1L).orElse(null);

        model.addAttribute("veiculo", veiculo);
        model.addAttribute("configuracao", configuracao);

        return "documentos/contrato-consignacao";
    }

    @GetMapping("/repasses/{id}/recibo")
    public String emitirRecibo(
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

        if (!"PAGO".equals(repasse.getStatus())) {
            return "redirect:/repasses/" + id;
        }

        List<PagamentoRepasse> pagamentos
                = pagamentoRepasseRepository
                        .findByRepasseIdOrderByDataPagamentoDesc(id);

        if (pagamentos.isEmpty()) {
            return "redirect:/repasses/" + id;
        }

        PagamentoRepasse ultimoPagamento = pagamentos.get(0);

        ConfiguracaoConcessionaria configuracao
                = configuracaoRepository.findById(1L).orElse(null);

        model.addAttribute("repasse", repasse);
        model.addAttribute("configuracao", configuracao);
        model.addAttribute("pagamentos", pagamentos);
        model.addAttribute(
                "dataQuitacao",
                ultimoPagamento.getDataPagamento()
        );

        return "documentos/recibo-quitacao";
    }
}