package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Cliente;
import br.com.urias.cadastro_veiculos.repository.ClienteRepository;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
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
public class PaginaClienteController {

    private final ClienteRepository clienteRepository;
    private final VendaRepository vendaRepository;

    public PaginaClienteController(
            ClienteRepository clienteRepository,
            VendaRepository vendaRepository) {

        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
    }

    @GetMapping("/compradores")
    public String formulario(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "false") boolean ajax,
            Model model) {

        Pageable pageable = PageRequest.of(
                pagina,
                5,
                Sort.by("id")
        );

        Page<Cliente> clientesPage;

        if (busca == null || busca.isBlank()) {

            clientesPage = clienteRepository.findAll(pageable);

        } else {

            clientesPage = clienteRepository
                    .findByNomeContainingIgnoreCaseOrCpfContainingIgnoreCaseOrTelefoneContainingIgnoreCase(
                            busca,
                            busca,
                            busca,
                            pageable
                    );
        }

        model.addAttribute("clientes", clientesPage.getContent());
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("totalPaginas", clientesPage.getTotalPages());
        model.addAttribute("busca", busca);

        model.addAttribute("vendaRepository", vendaRepository);

        model.addAttribute("paginaAtiva", "compradores");

        if (ajax) {
            return "cliente :: resultadosBusca";
        }

        return "cliente";
    }

    private boolean cpfValido(String cpf) {

        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (!cpf.matches("\\d{11}") || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            for (int digito = 9; digito < 11; digito++) {

                int soma = 0;

                for (int i = 0; i < digito; i++) {
                    soma += Character.getNumericValue(cpf.charAt(i))
                            * (digito + 1 - i);
                }

                int resultado = (soma * 10) % 11;

                if (resultado == 10) {
                    resultado = 0;
                }

                if (resultado != Character.getNumericValue(cpf.charAt(digito))) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/clientes/salvar")
    public String salvar(Cliente cliente, RedirectAttributes redirectAttributes) {

        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            redirectAttributes.addFlashAttribute("erro", "Informe o nome do comprador.");
            return "redirect:/compradores";
        }

        if (!cpfValido(cliente.getCpf())) {
            redirectAttributes.addFlashAttribute("erro", "CPF inválido. Verifique os números informados.");
            return "redirect:/compradores";
        }

        String telefone = cliente.getTelefone() == null
                ? ""
                : cliente.getTelefone().replaceAll("\\D", "");

        if (telefone.length() != 10 && telefone.length() != 11) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Telefone inválido. Informe o DDD e um número válido."
            );
            return "redirect:/compradores";
        }
        
        String email = cliente.getEmail();

        if (email != null && !email.isBlank()
                && !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "E-mail inválido. Verifique o endereço informado."
            );

            return "redirect:/compradores";
        }

        clienteRepository.save(cliente);

        return "redirect:/compradores";
    }

    @GetMapping("/clientes/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return "redirect:/compradores";
    }

    @GetMapping("/clientes/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);

        model.addAttribute("cliente", cliente);
        model.addAttribute("clientes", clienteRepository.findAll());

        return "cliente";
    }
}
