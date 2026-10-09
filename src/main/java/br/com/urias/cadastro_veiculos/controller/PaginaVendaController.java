package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Pagamento;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.model.StatusVeiculo;
import br.com.urias.cadastro_veiculos.repository.ClienteRepository;
import br.com.urias.cadastro_veiculos.repository.VeiculoRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.urias.cadastro_veiculos.model.TipoNotificacao;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import br.com.urias.cadastro_veiculos.service.NotificacaoService;
import java.util.List;

@Controller
public class PaginaVendaController {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoService notificacaoService;

    public PaginaVendaController(
            VendaRepository vendaRepository,
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            UsuarioRepository usuarioRepository,
            NotificacaoService notificacaoService) {

        this.vendaRepository = vendaRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/vendas/form")
    public String formulario(Model model, HttpSession session) {

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
               veiculoRepository.findByStatus(StatusVeiculo.DISPONIVEL)
        );

        model.addAttribute("paginaAtiva", "vendas");

        return "venda";
    }

    @Transactional
    @PostMapping("/vendas/salvar")
    public String salvar(
            Venda venda,
            Pagamento pagamento,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (venda.getCliente() == null) {
            return "redirect:/vendas/form";
        }

        if (venda.getVeiculo() == null) {
            return "redirect:/vendas/form";
        }

        if (venda.getDataVenda() == null) {
            return "redirect:/vendas/form";
        }

        if (venda.getValor() == null
                || venda.getValor() <= 0) {

            return "redirect:/vendas/form";
        }

        if (pagamento.getTipo() == null
                || pagamento.getTipo().isBlank()) {

            return "redirect:/vendas/form";
        }

        venda.setPagamento(pagamento);
        venda.setVendedor(usuarioLogado);

        venda.getVeiculo().setStatus(StatusVeiculo.VENDIDO);

        veiculoRepository.save(venda.getVeiculo());

        Venda vendaSalva = vendaRepository.save(venda);

        List<Usuario> administradores
                = usuarioRepository.findByPerfilIn(
                        List.of("DONO", "GERENTE")
                );

        for (Usuario administrador : administradores) {

            notificacaoService.criarNotificacao(
                    administrador,
                    TipoNotificacao.VENDA_REALIZADA,
                    "Venda realizada",
                    usuarioLogado.getNome()
                    + " realizou a venda do veículo "
                    + vendaSalva.getVeiculo().getNome()
                    + " para "
                    + vendaSalva.getCliente().getNome()
                    + ".",
                    null,
                    vendaSalva.getId()
            );
        }

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {

            return "redirect:/dashboard-vendedor?comprovante="
                    + vendaSalva.getId();
        }

        return "redirect:/admin/vendas?comprovante="
                + vendaSalva.getId();
    }

    @GetMapping("/vendas/sucesso/{id}")
    public String vendaSalvaComSucesso(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("vendaId", id);

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
            model.addAttribute(
                    "voltar",
                    "/dashboard-vendedor"
            );
        } else {
            model.addAttribute(
                    "voltar",
                    "/admin/vendas"
            );
        }

        return "venda-sucesso";
    }

    @GetMapping("/admin/vendas")
    public String historicoVendas(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int pagina,
            Model model,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
            return "redirect:/dashboard-vendedor";
        }

        Pageable pageable = PageRequest.of(
                pagina,
                5,
                Sort.by("id").descending()
        );

        Page<Venda> vendasPage;

        if (busca == null || busca.isBlank()) {

            vendasPage = vendaRepository.findAll(pageable);

        } else {

            vendasPage = vendaRepository
                    .findByClienteNomeContainingIgnoreCaseOrVeiculoNomeContainingIgnoreCaseOrVendedorNomeContainingIgnoreCase(
                            busca,
                            busca,
                            busca,
                            pageable
                    );
        }

        model.addAttribute(
                "vendas",
                vendasPage.getContent()
        );

        model.addAttribute(
                "paginaAtual",
                pagina
        );

        model.addAttribute(
                "totalPaginas",
                vendasPage.getTotalPages()
        );

        model.addAttribute("busca", busca);
        model.addAttribute("paginaAtiva", "vendas");

        return "admin-vendas";
    }
}
