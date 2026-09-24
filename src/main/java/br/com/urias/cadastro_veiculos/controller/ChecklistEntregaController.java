package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.ChecklistEntrega;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import br.com.urias.cadastro_veiculos.model.ItemChecklistEntrega;
import br.com.urias.cadastro_veiculos.repository.ItemChecklistEntregaRepository;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.repository.ChecklistEntregaRepository;
import java.time.LocalDate;

@Controller
public class ChecklistEntregaController {

    private final VendaRepository vendaRepository;
    private final ItemChecklistEntregaRepository itemChecklistEntregaRepository;
    private final ChecklistEntregaRepository checklistEntregaRepository;

    public ChecklistEntregaController(
        VendaRepository vendaRepository,
        ItemChecklistEntregaRepository itemChecklistEntregaRepository,
        ChecklistEntregaRepository checklistEntregaRepository) {

    this.vendaRepository = vendaRepository;
    this.itemChecklistEntregaRepository = itemChecklistEntregaRepository;
    this.checklistEntregaRepository = checklistEntregaRepository;
}

    @GetMapping("/entregas/{id}")
    public String abrirChecklist(
            @PathVariable Long id,
            Model model) {

        Venda venda = vendaRepository
                .findById(id)
                .orElse(null);

        if (venda == null) {
            return "redirect:/admin/vendas";
        }

        model.addAttribute("venda", venda);

        return "checklist-entrega";
    }

    private String redirecionarPorPerfil(HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario != null
                && usuario.getPerfil().equals("VENDEDOR")) {

            return "redirect:/dashboard-vendedor";
        }

        return "redirect:/admin/vendas";
    }

    @PostMapping("/entregas/{id}/confirmar")
    public String confirmarEntrega(
            @PathVariable Long id,
            @RequestParam(required = false) List<Long> itensConferidos,
            HttpSession session) {

        Venda venda = vendaRepository
                .findById(id)
                .orElse(null);

        if (venda == null) {
            return redirecionarPorPerfil(session);
        }

        if (venda.getVeiculo().getChecklistEntrega() == null) {
            return redirecionarPorPerfil(session);
        }

        List<ItemChecklistEntrega> itens
                = venda.getVeiculo()
                        .getChecklistEntrega()
                        .getItens();

        for (ItemChecklistEntrega item : itens) {

            boolean conferido = itensConferidos != null
                    && itensConferidos.contains(item.getId());

            item.setConferido(conferido);

            itemChecklistEntregaRepository.save(item);
        }

        return "redirect:/entregas/" + id;
    }

    @PostMapping("/entregas/{id}/concluir")
    public String concluirEntrega(
            @PathVariable Long id,
            HttpSession session) {

        Venda venda = vendaRepository
                .findById(id)
                .orElse(null);

        if (venda == null) {
            return redirecionarPorPerfil(session);
        }

        ChecklistEntrega checklist
                = venda.getVeiculo().getChecklistEntrega();

        if (checklist == null) {
            return redirecionarPorPerfil(session);
        }
      
        if (venda.getVeiculo()
                .getChecklistEntrega()
                .isEntregaConcluida()) {

            return "redirect:/entregas/" + id;
        }

        if (checklist.getItens().isEmpty()) {
            return "redirect:/entregas/" + id + "?erro=itens";
        }

        boolean todosConferidos = checklist
                .getItens()
                .stream()
                .allMatch(ItemChecklistEntrega::isConferido);

        if (!todosConferidos) {
            return "redirect:/entregas/" + id + "?erro=itens";
        }

        checklist.setEntregaConcluida(true);
        checklist.setDataEntrega(LocalDate.now());
        
        checklistEntregaRepository.save(checklist);

        return "redirect:/entregas/" + id;
    }
}
