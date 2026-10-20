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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    private boolean cpfValido(String cpf) {

        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (!cpf.matches("\\d{11}")
                || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        for (int tamanho = 9; tamanho < 11; tamanho++) {

            int soma = 0;

            for (int i = 0; i < tamanho; i++) {
                soma += Character.getNumericValue(cpf.charAt(i))
                        * (tamanho + 1 - i);
            }

            int resto = (soma * 10) % 11;
            int digito = resto == 10 ? 0 : resto;

            if (digito != Character.getNumericValue(cpf.charAt(tamanho))) {
                return false;
            }
        }

        return true;
    }

    @PostMapping("/veiculos/salvar")
    public String salvar(
            Veiculo veiculo,
            @RequestParam(required = false) List<String> itensChecklist,
            RedirectAttributes redirectAttributes) {

        if (veiculo.getNome() == null || veiculo.getNome().trim().isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Informe o nome do veículo."
            );

            return "redirect:/veiculos";
        }
        
        if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Informe a marca do veículo."
            );

            return "redirect:/veiculos";
        }
      
        if (veiculo.getCor() == null || veiculo.getCor().trim().isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Informe a cor do veículo."
            );

            return "redirect:/veiculos";
        }

        String placa = veiculo.getPlaca()
                .trim()
                .toUpperCase()
                .replace("-", "");

        boolean placaValida = placa.matches("[A-Z]{3}[0-9]{4}")
                || placa.matches("[A-Z]{3}[0-9][A-J][0-9]{2}");

        if (!placaValida) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Placa inválida! Informe uma placa no formato ABC1234 ou ABC1D23."
            );

            return "redirect:/veiculos";
        }

        int anoMaximo = LocalDate.now().getYear() + 1;

        if (veiculo.getAno() == null
                || veiculo.getAno() < 1900
                || veiculo.getAno() > anoMaximo) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Ano inválido! Informe um ano entre 1900 e " + anoMaximo + "."
            );

            return "redirect:/veiculos";
        }

        if (veiculo.getPreco() == null
                || !Double.isFinite(veiculo.getPreco())
                || veiculo.getPreco() <= 0) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Preço inválido! Informe um valor maior que zero."
            );

            return "redirect:/veiculos";
        }

        if (veiculo.getNomeProprietario() == null
                || veiculo.getNomeProprietario().trim().isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Informe o nome do proprietário do veículo."
            );

            return "redirect:/veiculos";
        }
        
        if (!cpfValido(veiculo.getCpfProprietario())) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "CPF do proprietário inválido! Verifique os números informados."
            );

            return "redirect:/veiculos";
        }
        
        String telefone = veiculo.getTelefoneProprietario();

        String telefoneNumeros = telefone == null
                ? ""
                : telefone.replaceAll("\\D", "");

        if (!telefoneNumeros.matches("[1-9][0-9][2-9][0-9]{7,8}")) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Telefone inválido! Informe um número com DDD e 10 ou 11 dígitos."
            );

            return "redirect:/veiculos";
        }
        
        String email = veiculo.getEmailProprietario();

        if (email != null && !email.isBlank()
                && !email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "E-mail inválido! Informe um endereço de e-mail válido."
            );

            return "redirect:/veiculos";
        }
        
        String emailProprietario = veiculo.getEmailProprietario();

        if (emailProprietario != null && !emailProprietario.isBlank()
                && !emailProprietario.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "E-mail inválido! Informe um endereço de e-mail válido."
            );

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

        if (itensChecklist != null && !itensChecklist.isEmpty()
                || checklistExistente != null) {
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