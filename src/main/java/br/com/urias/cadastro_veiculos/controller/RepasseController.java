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
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.urias.cadastro_veiculos.model.TipoNotificacao;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import br.com.urias.cadastro_veiculos.service.NotificacaoService;

@Controller
public class RepasseController {

    private final RepasseRepository repasseRepository;
    private final VendaRepository vendaRepository;
    private final PagamentoRepasseRepository pagamentoRepasseRepository;
    private final ConfiguracaoConcessionariaRepository configuracaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoService notificacaoService;

    public RepasseController(
            RepasseRepository repasseRepository,
            VendaRepository vendaRepository,
            PagamentoRepasseRepository pagamentoRepasseRepository,
            ConfiguracaoConcessionariaRepository configuracaoRepository,
            UsuarioRepository usuarioRepository,
            NotificacaoService notificacaoService) {

        this.repasseRepository = repasseRepository;
        this.vendaRepository = vendaRepository;
        this.pagamentoRepasseRepository = pagamentoRepasseRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoService = notificacaoService;
    }

    /*
     * =====================================================
     * LISTAGEM DE REPASSES
     * =====================================================
     */
    @GetMapping("/repasses")
    public String listar(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int pagina,
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

        /*
     * Impede números negativos na paginação.
         */
        if (pagina < 0) {
            pagina = 0;
        }

        String termoBusca = busca.trim().toLowerCase(Locale.ROOT);

        /*
     * Lista completa necessária para identificar:
     * - vendas que já possuem repasse;
     * - repasses ainda pendentes.
         */
        List<Repasse> repasses = repasseRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );

        List<Venda> vendas = vendaRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );

        /*
     * IDs das vendas que já possuem repasse.
         */
        Set<Long> vendasComRepasse = repasses.stream()
                .filter(repasse -> repasse.getVenda() != null)
                .map(repasse -> repasse.getVenda().getId())
                .collect(Collectors.toSet());

        /*
     * Vendas sem repasse iniciado e compatíveis com a busca.
         */
        List<Venda> vendasAguardandoRepasse = vendas.stream()
                .filter(venda
                        -> !vendasComRepasse.contains(venda.getId())
                )
                .filter(venda
                        -> correspondeBusca(venda, termoBusca)
                )
                .toList();

        /*
     * Repasses em andamento compatíveis com a busca.
         */
        List<Repasse> repassesPendentes = repasses.stream()
                .filter(repasse
                        -> !"PAGO".equals(repasse.getStatus())
                )
                .filter(repasse
                        -> correspondeBusca(
                        repasse.getVenda(),
                        termoBusca
                )
                )
                .toList();

        /*
     * Somente os finalizados são paginados.
         */
        Pageable pageable = PageRequest.of(pagina, 5);

        Page<Repasse> paginaFinalizados
                = repasseRepository.buscarFinalizados(
                        busca.trim(),
                        pageable
                );

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
                paginaFinalizados.getContent()
        );

        model.addAttribute(
                "paginaFinalizados",
                paginaFinalizados
        );

        model.addAttribute("busca", busca);
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

    /*
 * =====================================================
 * REGISTRAR PAGAMENTO DO REPASSE
 * =====================================================
     */
    @PostMapping("/repasses/pagar")
    public String registrarPagamento(
            @RequestParam Long repasseId,
            @RequestParam LocalDate dataPagamento,
            @RequestParam String formaPagamento,
            @RequestParam Double valor,
            @RequestParam(required = false) String observacao,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/";
        }

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
            return "redirect:/dashboard-vendedor";
        }

        Repasse repasse
                = repasseRepository.findById(repasseId).orElse(null);

        if (repasse == null) {
            return "redirect:/repasses";
        }

        if (valor == null || valor <= 0) {
            return "redirect:/repasses/" + repasseId;
        }

        if (valor > repasse.getSaldo()) {
            return "redirect:/repasses/" + repasseId;
        }

        PagamentoRepasse pagamento = new PagamentoRepasse();

        pagamento.setRepasse(repasse);
        pagamento.setDataPagamento(dataPagamento);
        pagamento.setFormaPagamento(formaPagamento);
        pagamento.setValor(valor);
        pagamento.setObservacao(observacao);
        
        // Saldo antes de registrar o pagamento
        Double saldoAnterior = repasse.getSaldo();

        pagamentoRepasseRepository.save(pagamento);
        
        // Verifica se este pagamento quitou o repasse
        boolean repasseQuitado
                = Double.compare(valor, repasse.getSaldo()) == 0;
        
        if (repasseQuitado) {

            List<Usuario> administradores
                    = usuarioRepository.findByPerfilIn(
                            List.of("DONO", "GERENTE")
                    );

            for (Usuario administrador : administradores) {

                notificacaoService.criarNotificacao(
                        administrador,
                        TipoNotificacao.REPASSE_QUITADO,
                        "Repasse quitado",
                        "O repasse do veículo "
                        + repasse.getVenda().getVeiculo().getNome()
                        + " foi quitado por "
                        + usuarioLogado.getNome()
                        + ".",
                        null,
                        repasse.getId()
                );
            }
        }

        return "redirect:/repasses/" + repasseId;
    }

    private boolean correspondeBusca(
            Venda venda,
            String termoBusca) {

        if (termoBusca.isBlank()) {
            return true;
        }

        if (venda == null || venda.getVeiculo() == null) {
            return false;
        }

        return contem(
                venda.getVeiculo().getNomeProprietario(),
                termoBusca
        )
                || contem(
                        venda.getVeiculo().getNome(),
                        termoBusca
                )
                || contem(
                        venda.getVeiculo().getPlaca(),
                        termoBusca
                );
    }

    private boolean contem(
            String texto,
            String termoBusca) {

        return texto != null
                && texto.toLowerCase(Locale.ROOT)
                        .contains(termoBusca);
    }
}
