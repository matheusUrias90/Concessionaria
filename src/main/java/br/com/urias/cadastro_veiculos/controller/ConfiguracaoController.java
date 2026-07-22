package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.ConfiguracaoConcessionaria;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.ConfiguracaoConcessionariaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConfiguracaoController {

    @Autowired
    private ConfiguracaoConcessionariaRepository configuracaoRepository;

    @GetMapping("/configuracao")
    public String abrirConfiguracao(Model model, HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!usuarioLogado.getPerfil().equals("DONO")) {
            return "redirect:/dashboard";
        }

        ConfiguracaoConcessionaria configuracao =
                configuracaoRepository.findAll()
                        .stream()
                        .findFirst()
                        .orElse(new ConfiguracaoConcessionaria());

        model.addAttribute("configuracao", configuracao);
        
        model.addAttribute("paginaAtiva", "configuracao");

        return "configuracao";
    }

    @PostMapping("/configuracao/salvar")
    public String salvarConfiguracao(@ModelAttribute ConfiguracaoConcessionaria configuracao,
                                     HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (!usuarioLogado.getPerfil().equals("DONO")) {
            return "redirect:/dashboard";
        }

        configuracaoRepository.save(configuracao);

        return "redirect:/admin";
    }
}