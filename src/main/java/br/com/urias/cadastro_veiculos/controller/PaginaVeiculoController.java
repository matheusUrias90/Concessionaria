package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.ChecklistEntrega;
import br.com.urias.cadastro_veiculos.model.ItemChecklistEntrega;
import br.com.urias.cadastro_veiculos.model.StatusVeiculo;
import br.com.urias.cadastro_veiculos.model.Veiculo;
import br.com.urias.cadastro_veiculos.repository.VeiculoRepository;
import java.time.LocalDate;
import java.util.List;
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
            @RequestParam(defaultValue = "false") boolean ajax,
            Model model) {

        Pageable pageable = PageRequest.of(
                Math.max(pagina, 0),
                5,
                Sort.by("id")
        );

        Page<Veiculo> veiculosPage;

        if (busca == null || busca.isBlank()) {
            veiculosPage = veiculoRepository.findByStatus(
                    StatusVeiculo.DISPONIVEL,
                    pageable
            );
        } else {
            veiculosPage = veiculoRepository
                    .findByStatusAndNomeContainingIgnoreCaseOrStatusAndMarcaContainingIgnoreCaseOrStatusAndPlacaContainingIgnoreCase(
                            StatusVeiculo.DISPONIVEL,
                            busca,
                            StatusVeiculo.DISPONIVEL,
                            busca,
                            StatusVeiculo.DISPONIVEL,
                            busca,
                            pageable
                    );
        }

        model.addAttribute("veiculos", veiculosPage.getContent());
        model.addAttribute("paginaAtual", Math.max(pagina, 0));
        model.addAttribute("totalPaginas", veiculosPage.getTotalPages());
        model.addAttribute("busca", busca);
        model.addAttribute("paginaAtiva", "veiculos");
        model.addAttribute("veiculo", new Veiculo());

        if (ajax) {
            return "veiculo :: resultadosBusca";
        }
        return "veiculo";
    }

    @PostMapping("/veiculos/salvar")
    public String salvar(
            Veiculo veiculo,
            @RequestParam(required = false) List<String> itensChecklist) {

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
        ChecklistEntrega checklistExistente = null;

        if (!novoVeiculo) {
            Veiculo veiculoExistente = veiculoRepository.findById(veiculo.getId()).orElse(null);
            if (veiculoExistente != null) {
                checklistExistente = veiculoExistente.getChecklistEntrega();
                veiculo.setDataEntrada(veiculoExistente.getDataEntrada());
                veiculo.setStatus(veiculoExistente.getStatus());
            }
        }

        if (novoVeiculo) {
            veiculo.setDataEntrada(LocalDate.now());
            veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        }

        if (itensChecklist != null && !itensChecklist.isEmpty()) {
            ChecklistEntrega checklist;
            if (checklistExistente != null) {
                checklist = checklistExistente;
                checklist.getItens().clear();
                checklist.setVeiculo(veiculo);
            } else {
                checklist = new ChecklistEntrega();
                checklist.setVeiculo(veiculo);
            }

            veiculo.setChecklistEntrega(checklist);
            for (String descricao : itensChecklist) {
                if (descricao != null && !descricao.isBlank()) {
                    ItemChecklistEntrega item = new ItemChecklistEntrega();
                    item.setDescricao(descricao.trim());
                    item.setConferido(false);
                    item.setChecklist(checklist);
                    checklist.getItens().add(item);
                }
            }
        }

        Veiculo veiculoSalvo = veiculoRepository.save(veiculo);
        if (novoVeiculo) {
            return "redirect:/veiculos?contrato=" + veiculoSalvo.getId();
        }
        return "redirect:/veiculos";
    }

    @GetMapping("/veiculos/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Veiculo veiculo = veiculoRepository.findById(id).orElse(null);
        model.addAttribute("veiculo", veiculo);
        model.addAttribute("veiculos", veiculoRepository.findByStatus(StatusVeiculo.DISPONIVEL));
        model.addAttribute("paginaAtiva", "veiculos");
        return "veiculo";
    }

    @GetMapping("/veiculos/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        veiculoRepository.deleteById(id);
        return "redirect:/veiculos";
    }
}