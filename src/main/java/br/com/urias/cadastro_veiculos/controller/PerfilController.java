package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.SolicitacaoAlteracaoLogin;
import br.com.urias.cadastro_veiculos.model.StatusSolicitacaoLogin;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.SolicitacaoAlteracaoLoginRepository;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.urias.cadastro_veiculos.model.TipoNotificacao;
import br.com.urias.cadastro_veiculos.service.NotificacaoService;
import java.util.List;

@Controller
public class PerfilController {

    private final SolicitacaoAlteracaoLoginRepository solicitacaoRepository;

    private final UsuarioRepository usuarioRepository;

    private final NotificacaoService notificacaoService;

    public PerfilController(
            SolicitacaoAlteracaoLoginRepository solicitacaoRepository,
            UsuarioRepository usuarioRepository,
            NotificacaoService notificacaoService) {

        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/meu-perfil")
    public String meuPerfil(
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuarioLogado);

        return "meu-perfil";
    }

    @GetMapping("/meu-perfil/solicitar-login")
    public String abrirSolicitacaoLogin(
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuarioLogado);

        return "solicitar-alteracao-login";
    }

    @GetMapping("/meu-perfil/alterar-senha")
    public String abrirAlterarSenha(HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        return "alterar-senha";
    }

    @PostMapping("/meu-perfil/alterar-senha")
    public String alterarSenha(
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha,
            @RequestParam String confirmacaoSenha,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!usuarioLogado.getSenha().equals(senhaAtual)) {
            return "redirect:/meu-perfil/alterar-senha?erro=senhaAtual";
        }

        if (!novaSenha.equals(confirmacaoSenha)) {
            return "redirect:/meu-perfil/alterar-senha?erro=confirmacao";
        }

        usuarioLogado.setSenha(novaSenha);
        usuarioRepository.save(usuarioLogado);
        
        List<Usuario> administradores
                = usuarioRepository.findByPerfilIn(
                        List.of("DONO", "GERENTE")
                );

        for (Usuario administrador : administradores) {

            notificacaoService.criarNotificacao(
                    administrador,
                    TipoNotificacao.SENHA_ALTERADA,
                    "Senha alterada",
                    usuarioLogado.getNome()
                    + " alterou a própria senha.",
                    null,
                    null
            );
        }

        session.setAttribute("usuarioLogado", usuarioLogado);

        return "redirect:/meu-perfil?senhaAlterada=true";
    }

    @PostMapping("/meu-perfil/solicitar-login")
    public String solicitarAlteracaoLogin(
            @RequestParam String novoLogin,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        novoLogin = novoLogin.trim();

        if (novoLogin.isBlank()) {
            return "redirect:/meu-perfil/solicitar-login?erro=vazio";
        }

        if (novoLogin.equals(usuarioLogado.getLogin())) {
            return "redirect:/meu-perfil/solicitar-login?erro=igual";
        }

        if (usuarioRepository.existsByLogin(novoLogin)) {
            return "redirect:/meu-perfil/solicitar-login?erro=existente";
        }

        if (solicitacaoRepository.existsByUsuarioIdAndStatus(
                usuarioLogado.getId(),
                StatusSolicitacaoLogin.PENDENTE)) {

            return "redirect:/meu-perfil/solicitar-login?erro=pendente";
        }

        SolicitacaoAlteracaoLogin solicitacao
                = new SolicitacaoAlteracaoLogin();

        solicitacao.setUsuario(usuarioLogado);
        solicitacao.setLoginAtual(usuarioLogado.getLogin());
        solicitacao.setNovoLogin(novoLogin);
        solicitacao.setDataSolicitacao(LocalDateTime.now());
        solicitacao.setStatus(StatusSolicitacaoLogin.PENDENTE);

        solicitacaoRepository.save(solicitacao);

        List<Usuario> administradores
                = usuarioRepository.findByPerfilIn(
                        List.of("DONO", "GERENTE")
                );

        for (Usuario administrador : administradores) {

            notificacaoService.criarNotificacao(
                    administrador,
                    TipoNotificacao.SOLICITACAO_LOGIN,
                    "Solicitação de alteração de login",
                    usuarioLogado.getNome()
                    + " solicitou a alteração do login de "
                    + usuarioLogado.getLogin()
                    + " para "
                    + novoLogin
                    + ".",
                    "/admin/solicitacoes-login",
                    solicitacao.getId()
            );
        }

        return "redirect:/meu-perfil?solicitacaoLogin=true";
    }
}
