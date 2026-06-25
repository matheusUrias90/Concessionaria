package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    public LoginController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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

        Usuario usuarioLogado =
                usuarioRepository.findByLoginAndSenha(login, senha);

        if (usuarioLogado == null) {

            model.addAttribute("erro", "Usuário ou senha inválidos");

            return "login";
        }

        session.setAttribute("usuarioLogado", usuarioLogado);

        if (usuarioLogado.getPerfil().equals("VENDEDOR")) {
            return "redirect:/dashboard-vendedor";
        }

        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}