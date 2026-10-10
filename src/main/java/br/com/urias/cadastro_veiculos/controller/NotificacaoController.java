package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Notificacao;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.NotificacaoRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import br.com.urias.cadastro_veiculos.service.NotificacaoService;
import br.com.urias.cadastro_veiculos.model.TipoNotificacao;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotificacaoController {

    private final NotificacaoRepository notificacaoRepository;
    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService,
            NotificacaoRepository notificacaoRepository) {
        this.notificacaoService = notificacaoService;
        this.notificacaoRepository = notificacaoRepository;
    }

    @GetMapping("/notificacoes")
    public String listar(
            @RequestParam(required = false) Boolean lida,
            @RequestParam(required = false) TipoNotificacao tipo,
            @RequestParam(defaultValue = "0") int pagina,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        // Busca notificações com filtros e paginação
        Page<Notificacao> notificacoesPage
                = notificacaoService.buscarComFiltros(
                        usuarioLogado.getId(),
                        lida,
                        tipo,
                        pagina
                );

        // Notificações da página atual
        model.addAttribute(
                "notificacoes",
                notificacoesPage.getContent()
        );

    // Informações da paginação
    model.addAttribute("paginaAtual", notificacoesPage.getNumber());
    model.addAttribute("totalPaginas", notificacoesPage.getTotalPages());
    model.addAttribute("totalNotificacoes", notificacoesPage.getTotalElements());

    // Mantém os filtros selecionados
    model.addAttribute("lidaSelecionada", lida);
    model.addAttribute("tipoSelecionado", tipo);

        // Disponibiliza os tipos para o select
        model.addAttribute(
                "tiposNotificacao",
                notificacaoRepository.buscarTiposPorUsuario(usuarioLogado.getId())
        );

    return "notificacoes";
}

    @PostMapping("/notificacoes/{id}/ler")
    public String marcarComoLida(
            @PathVariable Long id,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        notificacaoService.marcarComoLida(
                id,
                usuarioLogado.getId()
        );

        return "redirect:/notificacoes";
    }

    @PostMapping("/notificacoes/ler-todas")
    public String marcarTodasComoLidas(HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        notificacaoService.marcarTodasComoLidas(
                usuarioLogado.getId()
        );

        return "redirect:/notificacoes";
    }
}
