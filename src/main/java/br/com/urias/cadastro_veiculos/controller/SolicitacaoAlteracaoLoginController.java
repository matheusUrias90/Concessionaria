package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.SolicitacaoAlteracaoLogin;
import br.com.urias.cadastro_veiculos.model.StatusSolicitacaoLogin;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.SolicitacaoAlteracaoLoginRepository;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SolicitacaoAlteracaoLoginController {

    private final SolicitacaoAlteracaoLoginRepository solicitacaoRepository;

    private final UsuarioRepository usuarioRepository;

    public SolicitacaoAlteracaoLoginController(
            SolicitacaoAlteracaoLoginRepository solicitacaoRepository,
            UsuarioRepository usuarioRepository) {

        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/admin/solicitacoes-login/ocultar-aviso")
    @ResponseBody
    public void ocultarAviso(HttpSession session) {
        session.setAttribute("avisoSolicitacaoLogin", false);
    }

    @GetMapping("/admin/solicitacoes-login")
    public String listarSolicitacoes(
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!"DONO".equals(usuarioLogado.getPerfil())
                && !"GERENTE".equals(usuarioLogado.getPerfil())) {

            return "redirect:/dashboard-vendedor";
        }

        List<SolicitacaoAlteracaoLogin> solicitacoes
                = solicitacaoRepository
                        .findByStatusOrderByDataSolicitacaoDesc(
                                StatusSolicitacaoLogin.PENDENTE
                        );

        model.addAttribute("solicitacoes", solicitacoes);

        return "solicitacoes-login";
    }

    @PostMapping("/admin/solicitacoes-login/{id}/aprovar")
    public String aprovarSolicitacao(
            @PathVariable Long id,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!"DONO".equals(usuarioLogado.getPerfil())
                && !"GERENTE".equals(usuarioLogado.getPerfil())) {

            return "redirect:/dashboard-vendedor";
        }

        SolicitacaoAlteracaoLogin solicitacao
                = solicitacaoRepository.findById(id).orElse(null);

        if (solicitacao == null
                || solicitacao.getStatus() != StatusSolicitacaoLogin.PENDENTE) {

            return "redirect:/admin/solicitacoes-login";
        }

        Usuario usuario = solicitacao.getUsuario();

        if (usuarioRepository.existsByLogin(solicitacao.getNovoLogin())) {
            return "redirect:/admin/solicitacoes-login?erro=loginExistente";
        }

        usuario.setLogin(solicitacao.getNovoLogin());
        usuarioRepository.save(usuario);

        solicitacao.setStatus(StatusSolicitacaoLogin.APROVADA);
        solicitacaoRepository.save(solicitacao);

        return "redirect:/admin/solicitacoes-login?aprovada=true";
    }

    @PostMapping("/admin/solicitacoes-login/{id}/recusar")
    public String recusarSolicitacao(
            @PathVariable Long id,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!"DONO".equals(usuarioLogado.getPerfil())
                && !"GERENTE".equals(usuarioLogado.getPerfil())) {

            return "redirect:/dashboard-vendedor";
        }

        SolicitacaoAlteracaoLogin solicitacao
                = solicitacaoRepository.findById(id).orElse(null);

        if (solicitacao == null
                || solicitacao.getStatus() != StatusSolicitacaoLogin.PENDENTE) {

            return "redirect:/admin/solicitacoes-login";
        }

        solicitacao.setStatus(StatusSolicitacaoLogin.RECUSADA);
        solicitacaoRepository.save(solicitacao);

        return "redirect:/admin/solicitacoes-login?recusada=true";
    }
}
