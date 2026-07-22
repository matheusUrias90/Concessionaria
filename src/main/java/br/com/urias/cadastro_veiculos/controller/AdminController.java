package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.repository.ClienteRepository;
import br.com.urias.cadastro_veiculos.repository.VeiculoRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Venda;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class AdminController {

    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final VendaRepository vendaRepository;

    public AdminController(
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            VendaRepository vendaRepository) {

        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.vendaRepository = vendaRepository;
    }

    @GetMapping("/admin")
    public String dashboard(Model model, HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if (usuarioLogado.getPerfil().equals("VENDEDOR")) {
            return "redirect:/dashboard-vendedor";
        }
        
        Long totalVendas = vendaRepository.count();

        Double faturamento = vendaRepository.somarFaturamento();

        Double ticketMedio = totalVendas > 0
                ? faturamento / totalVendas
                : 0.0;

        model.addAttribute("totalClientes", clienteRepository.count());
        model.addAttribute("totalVeiculos", veiculoRepository.count());
        model.addAttribute("totalVendas", totalVendas);

        model.addAttribute("veiculosDisponiveis", veiculoRepository.countByVendidoFalse());
        model.addAttribute("veiculosVendidos", veiculoRepository.countByVendidoTrue());

        model.addAttribute("ultimasVendas", vendaRepository.findTop5ByOrderByIdDesc());

        model.addAttribute("faturamento", faturamento);
        model.addAttribute("ticketMedio", ticketMedio);
        model.addAttribute("valorEstoque", veiculoRepository.somarEstoqueDisponivel());
        
        model.addAttribute("paginaAtiva", "admin");

        return "admin";
    }

    @GetMapping("/dashboard-vendedor")
    public String dashboardVendedor(Model model, HttpSession session) {

        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        model.addAttribute("totalVeiculosDisponiveis", veiculoRepository.countByVendidoFalse());
        model.addAttribute("totalCompradores", clienteRepository.count());
       
        List<Venda> minhasVendas
                = vendaRepository.findByVendedorIdOrderByIdDesc(usuarioLogado.getId());

        double valorVendidoPorMim = minhasVendas.stream()
                .mapToDouble(Venda::getValor)
                .sum();

        model.addAttribute("minhasVendas", minhasVendas);
        model.addAttribute("totalMinhasVendas", minhasVendas.size());
        model.addAttribute("valorVendidoPorMim", valorVendidoPorMim);

        return "dashboard-vendedor";
    }
}
