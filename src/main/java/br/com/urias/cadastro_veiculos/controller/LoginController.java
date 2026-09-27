package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.StatusSolicitacaoLogin;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.ConfiguracaoConcessionariaRepository;
import br.com.urias.cadastro_veiculos.repository.SolicitacaoAlteracaoLoginRepository;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UsuarioRepository usuarioRepository;
    
    private final SolicitacaoAlteracaoLoginRepository solicitacaoRepository;

    @Autowired
    private ConfiguracaoConcessionariaRepository configuracaoRepository;

    public LoginController(UsuarioRepository usuarioRepository, 
                           SolicitacaoAlteracaoLoginRepository solicitacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.solicitacaoRepository = solicitacaoRepository;
    }

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(
            @RequestParam String login,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado
                = usuarioRepository.findByLoginAndSenha(login, senha);

        if (usuarioLogado == null) {

            model.addAttribute("erro", "Usuário ou senha inválidos");

            return "login";
        }

        session.setAttribute("usuarioLogado", usuarioLogado);

        if ("DONO".equals(usuarioLogado.getPerfil())
                || "GERENTE".equals(usuarioLogado.getPerfil())) {

            boolean possuiSolicitacoesPendentes
                    = solicitacaoRepository.existsByStatus(
                            StatusSolicitacaoLogin.PENDENTE
                    );

            session.setAttribute(
                    "avisoSolicitacaoLogin",
                    possuiSolicitacoesPendentes
            );
        }

        if (usuarioLogado.getPerfil().equals("DONO")) {

            if (configuracaoRepository.count() == 0) {
                return "redirect:/configuracao";
            }

            return "redirect:/admin";
        }

        if (usuarioLogado.getPerfil().equals("GERENTE")) {
            return "redirect:/admin";
        }

        return "redirect:/dashboard-vendedor";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
