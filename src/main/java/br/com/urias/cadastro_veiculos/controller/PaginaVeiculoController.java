package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Veiculo;
import br.com.urias.cadastro_veiculos.repository.VeiculoRepository;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaginaVeiculoController {

    private final VeiculoRepository veiculoRepository;

    public PaginaVeiculoController(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping("/veiculos")
    public String formulario(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int pagina,
            Model model) {

        Pageable pageable = PageRequest.of(pagina, 5, Sort.by("id"));

        Page<Veiculo> veiculosPage;

        if (busca == null || busca.isBlank()) {
            veiculosPage = veiculoRepository.findByVendidoFalse(pageable);
        } else {
            veiculosPage = veiculoRepository
                    .findByNomeContainingIgnoreCaseOrMarcaContainingIgnoreCaseOrPlacaContainingIgnoreCase(
                            busca, busca, busca, pageable
                    );
        }

        model.addAttribute("veiculos", veiculosPage.getContent());
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("totalPaginas", veiculosPage.getTotalPages());
        model.addAttribute("busca", busca);
        
        // Objeto vazio usado no formulário de novo veículo
        model.addAttribute("veiculo", new Veiculo());

        return "veiculo";
    }

    @PostMapping("/veiculos/salvar")
    public String salvar(Veiculo veiculo) {

        if (veiculo.getNome() == null || veiculo.getNome().trim().isEmpty()) {
            return "redirect:/veiculos";
        }

        if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {
            return "redirect:/veiculos";
        }

        if (veiculo.getCor() == null || veiculo.getCor().trim().isEmpty()) {
            return "redirect:/veiculos";
        }

        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            return "redirect:/veiculos";
        }

        if (veiculo.getAno() == null) {
            return "redirect:/veiculos";
        }

        if (veiculo.getPreco() == null || veiculo.getPreco() <= 0) {
            return "redirect:/veiculos";
        }

        boolean novoVeiculo = veiculo.getId() == null;

        if (novoVeiculo) {
            veiculo.setDataEntrada(LocalDate.now());
        }

        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);

        if (novoVeiculo) {
            return "redirect:/veiculos?contrato=" + veiculoSalvo.getId();
        }

        return "redirect:/veiculos";
    }

    @GetMapping("/veiculos/editar/{id}")
    public String editar(@PathVariable Long id,
            org.springframework.ui.Model model) {

        Veiculo veiculo
                = veiculoRepository.findById(id).orElse(null);

        model.addAttribute("veiculo", veiculo);
        model.addAttribute("veiculos",
                veiculoRepository.findByVendidoFalse());

        return "veiculo";
    }

    @GetMapping("/veiculos/excluir/{id}")
    public String excluir(@PathVariable Long id) {

        veiculoRepository.deleteById(id);

        return "redirect:/veiculos";
    }
}
