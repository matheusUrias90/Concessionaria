package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.service.NotificacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import br.com.urias.cadastro_veiculos.model.TipoNotificacao;

@ControllerAdvice
public class NotificacaoGlobalController {

    private final NotificacaoService notificacaoService;

    public NotificacaoGlobalController(
            NotificacaoService notificacaoService) {

        this.notificacaoService = notificacaoService;
    }

    @ModelAttribute
    public void adicionarNotificacoesNaoLidas(
            HttpSession session,
            Model model) {

        Usuario usuarioLogado =
                (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado != null) {

            long quantidade =
                    notificacaoService.contarNaoLidas(
                            usuarioLogado.getId()
                    );

            model.addAttribute(
                    "notificacoesNaoLidas",
                    quantidade
            );
        }
    }
}