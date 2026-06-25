package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private boolean semPermissao(HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        return usuarioLogado == null
                || (!usuarioLogado.getPerfil().equals("DONO")
                && !usuarioLogado.getPerfil().equals("GERENTE"));
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int pagina,
            Model model,
            HttpSession session
    ) {

        if (semPermissao(session)) {
            return "redirect:/dashboard-vendedor";
        }

        Page<Usuario> usuarios;

        if (busca.isEmpty()) {
            usuarios = usuarioRepository.findAll(PageRequest.of(pagina, 5));
        } else {
            usuarios = usuarioRepository
                    .findByNomeContainingIgnoreCaseOrLoginContainingIgnoreCase(
                            busca,
                            busca,
                            PageRequest.of(pagina, 5)
                    );
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("busca", busca);

        return "usuarios";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute Usuario novoUsuario, HttpSession session) {

        if (semPermissao(session)) {
            return "redirect:/dashboard-vendedor";
        }

        usuarioRepository.save(novoUsuario);

        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/excluir/{id}")
    public String excluir(@PathVariable Long id,
            HttpSession session) {

        if (semPermissao(session)) {
            return "redirect:/dashboard-vendedor";
        }

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getId().equals(id)) {
            return "redirect:/usuarios";
        }

        usuarioRepository.deleteById(id);

        return "redirect:/usuarios";
    }
}
