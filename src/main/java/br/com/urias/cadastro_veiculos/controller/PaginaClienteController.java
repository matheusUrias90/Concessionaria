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

        return "cliente";
    }

    @PostMapping("/clientes/salvar")
    public String salvar(Cliente cliente) {

        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            return "redirect:/compradores";
        }

        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            return "redirect:/compradores";
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty()) {
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
