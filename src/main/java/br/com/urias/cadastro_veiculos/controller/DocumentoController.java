package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.ConfiguracaoConcessionaria;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.repository.ConfiguracaoConcessionariaRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DocumentoController {

    private final VendaRepository vendaRepository;
    private final ConfiguracaoConcessionariaRepository configuracaoRepository;

    public DocumentoController(
            VendaRepository vendaRepository,
            ConfiguracaoConcessionariaRepository configuracaoRepository) {

        this.vendaRepository = vendaRepository;
        this.configuracaoRepository = configuracaoRepository;
    }

    @GetMapping("/documentos/comprovante-venda/{id}")
    public String comprovanteVenda(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Usuario usuarioLogado =
                (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Venda venda = vendaRepository.findById(id).orElse(null);

        if (venda == null) {

            if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {
                return "redirect:/dashboard-vendedor";
            }

            return "redirect:/admin/vendas";
        }

        if ("VENDEDOR".equals(usuarioLogado.getPerfil())) {

            if (venda.getVendedor() == null
                    || !venda.getVendedor().getId()
                            .equals(usuarioLogado.getId())) {

                return "redirect:/dashboard-vendedor";
            }
        }

        ConfiguracaoConcessionaria configuracao =
                configuracaoRepository.findById(1L).orElse(null);

        model.addAttribute("venda", venda);
        model.addAttribute("cliente", venda.getCliente());
        model.addAttribute("veiculo", venda.getVeiculo());
        model.addAttribute("pagamento", venda.getPagamento());
        model.addAttribute("vendedor", venda.getVendedor());
        model.addAttribute("configuracao", configuracao);

        return "documentos/comprovante-venda";
    }
}