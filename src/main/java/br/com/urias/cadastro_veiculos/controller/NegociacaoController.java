package br.com.urias.cadastro_veiculos.controller;

import br.com.urias.cadastro_veiculos.model.Cliente;
import br.com.urias.cadastro_veiculos.model.FormaPagamentoNegociacao;
import br.com.urias.cadastro_veiculos.model.Negociacao;
import br.com.urias.cadastro_veiculos.model.PendenciaNegociacao;
import br.com.urias.cadastro_veiculos.model.StatusFormaPagamento;
import br.com.urias.cadastro_veiculos.model.StatusNegociacao;
import br.com.urias.cadastro_veiculos.model.StatusPendencia;
import br.com.urias.cadastro_veiculos.model.StatusVeiculo;
import br.com.urias.cadastro_veiculos.model.Usuario;
import br.com.urias.cadastro_veiculos.model.Veiculo;
import br.com.urias.cadastro_veiculos.dto.ConclusaoNegociacaoDTO;
import br.com.urias.cadastro_veiculos.dto.VeiculoEntradaCadastroDTO;
import br.com.urias.cadastro_veiculos.model.ChecklistEntrega;
import br.com.urias.cadastro_veiculos.model.ItemChecklistEntrega;
import br.com.urias.cadastro_veiculos.repository.ClienteRepository;
import br.com.urias.cadastro_veiculos.repository.NegociacaoRepository;
import br.com.urias.cadastro_veiculos.repository.VeiculoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.urias.cadastro_veiculos.model.TipoFormaPagamento;
import br.com.urias.cadastro_veiculos.model.TipoPendencia;
import br.com.urias.cadastro_veiculos.model.VeiculoEntrada;
import java.util.HashSet;
import java.util.Set;
import br.com.urias.cadastro_veiculos.model.Venda;
import br.com.urias.cadastro_veiculos.repository.VendaRepository;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Comparator;
import org.springframework.data.domain.Sort;
import br.com.urias.cadastro_veiculos.model.TipoNotificacao;
import br.com.urias.cadastro_veiculos.repository.PendenciaNegociacaoRepository;
import br.com.urias.cadastro_veiculos.repository.UsuarioRepository;
import br.com.urias.cadastro_veiculos.service.NotificacaoService;

@Controller
public class NegociacaoController {

    private final NegociacaoRepository negociacaoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final VendaRepository vendaRepository;
    private final NotificacaoService notificacaoService;
    private final UsuarioRepository usuarioRepository;
    private final PendenciaNegociacaoRepository pendenciaNegociacaoRepository;

    public NegociacaoController(
            NegociacaoRepository negociacaoRepository,
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            VendaRepository vendaRepository,
            NotificacaoService notificacaoService,
            UsuarioRepository usuarioRepository,
            PendenciaNegociacaoRepository pendenciaNegociacaoRepository) {

        this.negociacaoRepository = negociacaoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.vendaRepository = vendaRepository;
        this.notificacaoService = notificacaoService;
        this.usuarioRepository = usuarioRepository;
        this.pendenciaNegociacaoRepository = pendenciaNegociacaoRepository;
    }

    @GetMapping("/negociacoes/nova")
    public String novaNegociacao(Model model, HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        List<Cliente> clientes = clienteRepository.findAll(
                Sort.by(Sort.Direction.ASC, "nome")
        );

        List<Veiculo> veiculos = new ArrayList<>(
                veiculoRepository.findByStatus(
                        StatusVeiculo.DISPONIVEL
                )
        );

        veiculos.sort(
                Comparator.comparing(
                        Veiculo::getNome,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        model.addAttribute("clientes", clientes);
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("paginaAtiva", "negociacoes");

        return "negociacao-form";
    }

    @PostMapping("/negociacoes/iniciar")
    @Transactional
    public String iniciarNegociacao(
            @RequestParam Long clienteId,
            @RequestParam Long veiculoId,
            @ModelAttribute Negociacao negociacao,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        validarComposicaoPagamento(negociacao);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Comprador não encontrado."
                )
                );

        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Veículo não encontrado."
                )
                );

        if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL) {
            throw new IllegalStateException(
                    "Este veículo não está mais disponível."
            );
        }

        negociacao.setCliente(cliente);
        negociacao.setVeiculo(veiculo);
        negociacao.setVendedor(usuarioLogado);
        negociacao.setDataInicio(LocalDateTime.now());
        negociacao.setStatus(StatusNegociacao.EM_ANDAMENTO);

        for (FormaPagamentoNegociacao forma
                : negociacao.getFormasPagamento()) {

            forma.setStatus(StatusFormaPagamento.PENDENTE);
            forma.setNegociacao(negociacao);
        }

        veiculo.setStatus(StatusVeiculo.RESERVADO);

        negociacaoRepository.save(negociacao);
        veiculoRepository.save(veiculo);

        List<Usuario> administradores
                = usuarioRepository.findByPerfilIn(
                        List.of("DONO", "GERENTE")
                );

        for (Usuario administrador : administradores) {

            notificacaoService.criarNotificacao(
                    administrador,
                    TipoNotificacao.NEGOCIACAO_INICIADA,
                    "Negociação iniciada",
                    usuarioLogado.getNome()
                    + " iniciou uma negociação com "
                    + cliente.getNome()
                    + " para o veículo "
                    + veiculo.getNome()
                    + ".",
                    null,
                    negociacao.getId()
            );
        }

        return "redirect:/negociacoes";
    }

    @GetMapping("/negociacoes")
    public String listarNegociacoes(
            Model model,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Long vendedorId = null;

        if ("VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil()))) {

            vendedorId = usuarioLogado.getId();
        }

        Page<Negociacao> paginaInicial
                = negociacaoRepository.filtrarNegociacoes(
                        StatusNegociacao.EM_ANDAMENTO,
                        "",
                        vendedorId,
                        PageRequest.of(0, 5)
                );

        model.addAttribute(
                "negociacoes",
                paginaInicial.getContent()
        );

        model.addAttribute("paginaAtiva", "negociacoes");

        return "negociacoes";
    }

    @GetMapping("/negociacoes/{id}")
    public String acompanharNegociacao(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Negociacao negociacao = negociacaoRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Negociação não encontrada."
                )
                );

        boolean vendedor
                = "VENDEDOR".equals(
                        String.valueOf(usuarioLogado.getPerfil())
                );

        boolean negociacaoDeOutroVendedor
                = !negociacao.getVendedor().getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            return "redirect:/negociacoes";
        }

        boolean possuiFormaRecusada
                = negociacao.getFormasPagamento()
                        .stream()
                        .anyMatch(forma
                                -> forma.getStatus()
                        == StatusFormaPagamento.RECUSADA
                        );

        boolean possuiFormaPendente
                = negociacao.getFormasPagamento()
                        .stream()
                        .anyMatch(forma
                                -> forma.getStatus()
                        == StatusFormaPagamento.PENDENTE
                        );

        boolean todasFormasConfirmadas
                = !negociacao.getFormasPagamento().isEmpty()
                && negociacao.getFormasPagamento()
                        .stream()
                        .allMatch(forma
                                -> forma.getStatus()
                        == StatusFormaPagamento.CONFIRMADA
                        );

        model.addAttribute("negociacao", negociacao);
        model.addAttribute("paginaAtiva", "negociacoes");
        model.addAttribute(
                "possuiFormaRecusada",
                possuiFormaRecusada
        );

        model.addAttribute(
                "possuiFormaPendente",
                possuiFormaPendente
        );

        model.addAttribute(
                "todasFormasConfirmadas",
                todasFormasConfirmadas
        );

        return "negociacao-detalhes";
    }

    @GetMapping("/negociacoes/{id}/editar")
    public String editarNegociacao(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Negociacao negociacao = negociacaoRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Negociação não encontrada."
                )
                );

        boolean vendedor
                = "VENDEDOR".equals(
                        String.valueOf(usuarioLogado.getPerfil())
                );

        boolean negociacaoDeOutroVendedor
                = !negociacao.getVendedor().getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            return "redirect:/negociacoes";
        }

        if (negociacao.getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return "redirect:/negociacoes/" + id;
        }

        List<Veiculo> veiculos = new ArrayList<>(
                veiculoRepository.findByStatus(
                        StatusVeiculo.DISPONIVEL
                )
        );

        boolean possuiVeiculoAtual = veiculos.stream()
                .anyMatch(v
                        -> v.getId().equals(
                        negociacao.getVeiculo().getId()
                )
                );

        if (!possuiVeiculoAtual) {
            veiculos.add(negociacao.getVeiculo());
        }

        veiculos.sort(
                Comparator.comparing(
                        Veiculo::getNome,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        model.addAttribute("negociacao", negociacao);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("veiculos", veiculos);
        model.addAttribute("editando", true);
        model.addAttribute("paginaAtiva", "negociacoes");

        return "negociacao-form";
    }

    @PostMapping("/negociacoes/{id}/editar")
    @Transactional
    public String salvarEdicaoNegociacao(
            @PathVariable Long id,
            @RequestParam Long clienteId,
            @RequestParam Long veiculoId,
            @ModelAttribute Negociacao dadosEditados,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Negociacao negociacao = negociacaoRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Negociação não encontrada."
                )
                );

        boolean vendedor = "VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil())
        );

        boolean negociacaoDeOutroVendedor
                = !negociacao.getVendedor().getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            return "redirect:/negociacoes";
        }

        if (negociacao.getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return "redirect:/negociacoes/" + id;
        }

        validarComposicaoPagamento(dadosEditados);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Comprador não encontrado."
                )
                );

        Veiculo novoVeiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Veículo não encontrado."
                )
                );

        Veiculo veiculoAnterior = negociacao.getVeiculo();

        boolean trocouVeiculo
                = !veiculoAnterior.getId()
                        .equals(novoVeiculo.getId());

        if (trocouVeiculo
                && novoVeiculo.getStatus()
                != StatusVeiculo.DISPONIVEL) {

            throw new IllegalStateException(
                    "O novo veículo não está disponível."
            );
        }

        if (trocouVeiculo) {
            veiculoAnterior.setStatus(StatusVeiculo.DISPONIVEL);
            novoVeiculo.setStatus(StatusVeiculo.RESERVADO);

            veiculoRepository.save(veiculoAnterior);
            veiculoRepository.save(novoVeiculo);
        }

        negociacao.setCliente(cliente);
        negociacao.setVeiculo(novoVeiculo);
        negociacao.setValorProposto(
                dadosEditados.getValorProposto()
        );

        atualizarFormasPagamento(
                negociacao,
                dadosEditados.getFormasPagamento()
        );

        negociacaoRepository.save(negociacao);

        return "redirect:/negociacoes/" + id;
    }

    private void atualizarFormasPagamento(
            Negociacao negociacao,
            List<FormaPagamentoNegociacao> formasEnviadas) {

        Set<Long> idsMantidos = new HashSet<>();

        for (FormaPagamentoNegociacao formaEnviada
                : formasEnviadas) {

            FormaPagamentoNegociacao forma;

            if (formaEnviada.getId() != null) {

                forma = negociacao.getFormasPagamento()
                        .stream()
                        .filter(existente
                                -> existente.getId().equals(
                                formaEnviada.getId()
                        )
                        )
                        .findFirst()
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Forma de pagamento inválida."
                        )
                        );

                idsMantidos.add(forma.getId());

            } else {

                forma = new FormaPagamentoNegociacao();
                negociacao.adicionarFormaPagamento(forma);
            }

            copiarDadosFormaPagamento(
                    forma,
                    formaEnviada
            );
        }

        negociacao.getFormasPagamento()
                .removeIf(forma
                        -> forma.getId() != null
                && !idsMantidos.contains(forma.getId())
                );
    }

    private void copiarDadosFormaPagamento(
            FormaPagamentoNegociacao destino,
            FormaPagamentoNegociacao origem) {

        destino.setTipo(origem.getTipo());
        destino.setValor(origem.getValor());
        destino.setStatus(StatusFormaPagamento.PENDENTE);
        destino.setDataPrevista(origem.getDataPrevista());
        destino.setParcelas(origem.getParcelas());
        destino.setValorParcela(origem.getValorParcela());
        destino.setInstituicaoFinanceira(
                origem.getInstituicaoFinanceira()
        );
        destino.setAdministradoraConsorcio(
                origem.getAdministradoraConsorcio()
        );
        destino.setNumeroCartaCredito(
                origem.getNumeroCartaCredito()
        );
        destino.setBandeiraCartao(
                origem.getBandeiraCartao()
        );

        if (origem.getTipo()
                == TipoFormaPagamento.VEICULO_ENTRADA) {

            copiarVeiculoEntrada(destino, origem);

        } else {

            destino.setVeiculoEntrada(null);
        }
    }

    private void copiarVeiculoEntrada(
            FormaPagamentoNegociacao destino,
            FormaPagamentoNegociacao origem) {

        if (origem.getVeiculoEntrada() == null) {
            throw new IllegalArgumentException(
                    "Informe os dados do veículo de entrada."
            );
        }

        VeiculoEntrada veiculoDestino
                = destino.getVeiculoEntrada();

        if (veiculoDestino == null) {
            veiculoDestino = new VeiculoEntrada();
            destino.setVeiculoEntrada(veiculoDestino);
        }

        VeiculoEntrada veiculoOrigem
                = origem.getVeiculoEntrada();

        veiculoDestino.setNome(veiculoOrigem.getNome());
        veiculoDestino.setMarca(veiculoOrigem.getMarca());
        veiculoDestino.setCor(veiculoOrigem.getCor());
        veiculoDestino.setPlaca(veiculoOrigem.getPlaca());
        veiculoDestino.setAno(veiculoOrigem.getAno());
    }

    @GetMapping("/negociacoes/{id}/veiculos-entrada/cadastrar")
    public String abrirCadastroVeiculosEntrada(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Negociacao negociacao
                = negociacaoRepository.findById(id)
                        .orElseThrow(()
                                -> new IllegalArgumentException(
                                "Negociação não encontrada."
                        )
                        );

        boolean vendedor = "VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil())
        );

        boolean negociacaoDeOutroVendedor
                = !negociacao.getVendedor()
                        .getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            return "redirect:/negociacoes";
        }

        if (negociacao.getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return "redirect:/negociacoes/" + id;
        }

        boolean todasConfirmadas
                = !negociacao.getFormasPagamento().isEmpty();

        for (FormaPagamentoNegociacao forma
                : negociacao.getFormasPagamento()) {

            if (forma.getStatus()
                    != StatusFormaPagamento.CONFIRMADA) {

                todasConfirmadas = false;
                break;
            }
        }

        if (!todasConfirmadas) {
            return "redirect:/negociacoes/" + id;
        }

        List<FormaPagamentoNegociacao> formasVeiculoEntrada
                = new ArrayList<>();

        for (FormaPagamentoNegociacao forma
                : negociacao.getFormasPagamento()) {

            if (forma.getTipo()
                    == TipoFormaPagamento.VEICULO_ENTRADA
                    && forma.getVeiculoEntrada() != null) {

                formasVeiculoEntrada.add(forma);
            }
        }

        if (formasVeiculoEntrada.isEmpty()) {
            return "redirect:/negociacoes/" + id;
        }

        model.addAttribute(
                "negociacao",
                negociacao
        );

        model.addAttribute(
                "formasVeiculoEntrada",
                formasVeiculoEntrada
        );

        model.addAttribute(
                "conclusao",
                new ConclusaoNegociacaoDTO()
        );

        model.addAttribute(
                "paginaAtiva",
                "negociacoes"
        );

        return "negociacao-veiculos-entrada";
    }

    @PostMapping("/negociacoes/{id}/veiculos-entrada/cadastrar")
    @Transactional
    public String cadastrarVeiculosEntradaEConcluir(
            @PathVariable Long id,
            @ModelAttribute ConclusaoNegociacaoDTO conclusao,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Negociacao negociacao = negociacaoRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Negociação não encontrada."
                )
                );

        boolean vendedor = "VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil())
        );

        boolean negociacaoDeOutroVendedor
                = !negociacao.getVendedor().getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            return "redirect:/negociacoes";
        }

        if (negociacao.getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return "redirect:/negociacoes/" + id;
        }

        validarComposicaoPagamento(negociacao);

        for (FormaPagamentoNegociacao forma
                : negociacao.getFormasPagamento()) {

            if (forma.getStatus()
                    != StatusFormaPagamento.CONFIRMADA) {

                return "redirect:/negociacoes/" + id;
            }
        }

        if (conclusao.getVeiculosEntrada() == null
                || conclusao.getVeiculosEntrada().isEmpty()) {

            throw new IllegalArgumentException(
                    "Informe os veículos de entrada."
            );
        }

        int quantidadeVeiculosEntrada = 0;

        /*
     * Percorre as formas de pagamento da negociação.
         */
        for (FormaPagamentoNegociacao forma
                : negociacao.getFormasPagamento()) {

            if (forma.getVeiculoEntrada() == null) {
                continue;
            }

            quantidadeVeiculosEntrada++;

            VeiculoEntradaCadastroDTO dadosCadastro = null;

            /*
         * Procura o preço informado para esta forma de pagamento.
             */
            for (VeiculoEntradaCadastroDTO item
                    : conclusao.getVeiculosEntrada()) {

                if (item.getFormaPagamentoId() != null
                        && item.getFormaPagamentoId()
                                .equals(forma.getId())) {

                    dadosCadastro = item;
                    break;
                }
            }

            if (dadosCadastro == null) {
                throw new IllegalArgumentException(
                        "Informe o preço de venda de todos "
                        + "os veículos de entrada."
                );
            }

            if (dadosCadastro.getPrecoVenda() == null
                    || dadosCadastro.getPrecoVenda() <= 0) {

                throw new IllegalArgumentException(
                        "O preço de venda do veículo deve "
                        + "ser maior que zero."
                );
            }

            VeiculoEntrada entrada = forma.getVeiculoEntrada();

            Veiculo veiculoEstoque = new Veiculo();

            veiculoEstoque.setNome(entrada.getNome());
            veiculoEstoque.setMarca(entrada.getMarca());
            veiculoEstoque.setCor(entrada.getCor());
            veiculoEstoque.setPlaca(entrada.getPlaca());
            veiculoEstoque.setAno(entrada.getAno());

            veiculoEstoque.setNomeProprietario(
                    negociacao.getCliente().getNome()
            );

            veiculoEstoque.setCpfProprietario(
                    negociacao.getCliente().getCpf()
            );

            veiculoEstoque.setTelefoneProprietario(
                    negociacao.getCliente().getTelefone()
            );

            veiculoEstoque.setEmailProprietario(
                    negociacao.getCliente().getEmail()
            );

            /*
         * Preço para revenda. O formulário começa preenchido
         * com o valor negociado, mas permite alteração.
             */
            veiculoEstoque.setPreco(dadosCadastro.getPrecoVenda());

            veiculoEstoque.setStatus(StatusVeiculo.DISPONIVEL);
            veiculoEstoque.setDataEntrada(LocalDate.now());

            ChecklistEntrega checklist = new ChecklistEntrega();
            checklist.setVeiculo(veiculoEstoque);

            if (dadosCadastro.getItensChecklist() != null) {

                for (String descricao : dadosCadastro.getItensChecklist()) {

                    if (descricao == null || descricao.isBlank()) {
                        continue;
                    }

                    ItemChecklistEntrega item = new ItemChecklistEntrega();

                    item.setDescricao(descricao.trim());
                    item.setConferido(false);
                    item.setChecklist(checklist);

                    checklist.getItens().add(item);
                }
            }

            veiculoEstoque.setChecklistEntrega(checklist);

            veiculoRepository.save(veiculoEstoque);
        }

        if (quantidadeVeiculosEntrada
                != conclusao.getVeiculosEntrada().size()) {

            throw new IllegalArgumentException(
                    "Existem dados de veículos que não pertencem "
                    + "a esta negociação."
            );
        }

        Venda venda = finalizarNegociacao(negociacao);

// Notifica DONO e GERENTE sobre a venda realizada
        List<Usuario> administradores
                = usuarioRepository.findByPerfilIn(
                        List.of("DONO", "GERENTE")
                );

        for (Usuario administrador : administradores) {

            notificacaoService.criarNotificacao(
                    administrador,
                    TipoNotificacao.VENDA_REALIZADA,
                    "Venda realizada",
                    negociacao.getVendedor().getNome()
                    + " realizou a venda do veículo "
                    + negociacao.getVeiculo().getNome()
                    + " para "
                    + negociacao.getCliente().getNome()
                    + ".",
                    null,
                    venda.getId()
            );
        }

        return "redirect:/negociacoes";
    }

    private Venda finalizarNegociacao(Negociacao negociacao) {

        if (vendaRepository
                .findByNegociacaoId(negociacao.getId())
                .isPresent()) {

            throw new IllegalStateException(
                    "Esta negociação já gerou uma venda."
            );
        }

        Venda venda = new Venda();

        venda.setCliente(negociacao.getCliente());
        venda.setVendedor(negociacao.getVendedor());
        venda.setVeiculo(negociacao.getVeiculo());
        venda.setDataVenda(LocalDate.now());
        venda.setValor(negociacao.getValorProposto());
        venda.setPagamento(null);
        venda.setNegociacao(negociacao);

        negociacao.setStatus(StatusNegociacao.CONCLUIDA);
        negociacao.setDataEncerramento(LocalDateTime.now());

        Veiculo veiculoVendido = negociacao.getVeiculo();
        veiculoVendido.setStatus(StatusVeiculo.VENDIDO);

        vendaRepository.save(venda);
        negociacaoRepository.save(negociacao);
        veiculoRepository.save(veiculoVendido);

        return venda;
    }

    @PostMapping("/negociacoes/{id}/concluir")
    @Transactional
    public String concluirNegociacao(
            @PathVariable Long id,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Negociacao negociacao = negociacaoRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Negociação não encontrada."
                )
                );

        boolean vendedor = "VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil())
        );

        boolean negociacaoDeOutroVendedor
                = !negociacao.getVendedor().getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            return "redirect:/negociacoes";
        }

        if (negociacao.getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return "redirect:/negociacoes/" + id;
        }

        validarComposicaoPagamento(negociacao);

        boolean todasConfirmadas
                = !negociacao.getFormasPagamento().isEmpty()
                && negociacao.getFormasPagamento()
                        .stream()
                        .allMatch(forma
                                -> forma.getStatus()
                        == StatusFormaPagamento.CONFIRMADA
                        );

        if (!todasConfirmadas) {
            return "redirect:/negociacoes/" + id;
        }

        boolean possuiVeiculoEntrada
                = negociacao.getFormasPagamento()
                        .stream()
                        .anyMatch(forma
                                -> forma.getVeiculoEntrada() != null
                        );

        if (possuiVeiculoEntrada) {
            return "redirect:/negociacoes/"
                    + id
                    + "/veiculos-entrada/cadastrar";
        }

        if (vendaRepository.findByNegociacaoId(id).isPresent()) {
            return "redirect:/negociacoes/" + id;
        }

        Venda venda = finalizarNegociacao(negociacao);

// Notifica DONO e GERENTE sobre a venda realizada
        List<Usuario> administradores
                = usuarioRepository.findByPerfilIn(
                        List.of("DONO", "GERENTE")
                );

        for (Usuario administrador : administradores) {

            notificacaoService.criarNotificacao(
                    administrador,
                    TipoNotificacao.VENDA_REALIZADA,
                    "Venda realizada",
                    negociacao.getVendedor().getNome()
                    + " realizou a venda do veículo "
                    + negociacao.getVeiculo().getNome()
                    + " para "
                    + negociacao.getCliente().getNome()
                    + ".",
                    null,
                    venda.getId()
            );
        }

        return "redirect:/entregas/" + venda.getId();
    }

    @GetMapping("/negociacoes/{id}/cancelar")
    public String abrirCancelamento(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Negociacao negociacao = negociacaoRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Negociação não encontrada."
                )
                );

        boolean vendedor = "VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil())
        );

        boolean negociacaoDeOutroVendedor
                = !negociacao.getVendedor().getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            return "redirect:/negociacoes";
        }

        if (negociacao.getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return "redirect:/negociacoes/" + id;
        }

        model.addAttribute("negociacao", negociacao);
        model.addAttribute("paginaAtiva", "negociacoes");

        return "negociacao-cancelar";
    }

    @PostMapping("/negociacoes/{id}/cancelar")
    @Transactional
    public String cancelarNegociacao(
            @PathVariable Long id,
            @RequestParam String motivoCancelamento,
            @RequestParam(required = false) List<Long> formasParaDevolver,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Negociacao negociacao = negociacaoRepository.findById(id)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "Negociação não encontrada."
                )
                );

        boolean vendedor = "VENDEDOR".equals(
                String.valueOf(usuarioLogado.getPerfil())
        );

        boolean negociacaoDeOutroVendedor
                = !negociacao.getVendedor().getId()
                        .equals(usuarioLogado.getId());

        if (vendedor && negociacaoDeOutroVendedor) {
            return "redirect:/negociacoes";
        }

        if (negociacao.getStatus()
                != StatusNegociacao.EM_ANDAMENTO) {

            return "redirect:/negociacoes/" + id;
        }

        if (motivoCancelamento == null
                || motivoCancelamento.isBlank()) {

            throw new IllegalArgumentException(
                    "Informe o motivo do cancelamento."
            );
        }

        List<PendenciaNegociacao> pendenciasCriadas = new ArrayList<>();

        if (formasParaDevolver != null) {

            for (Long formaId : formasParaDevolver) {

                FormaPagamentoNegociacao forma
                        = negociacao.getFormasPagamento()
                                .stream()
                                .filter(item
                                        -> item.getId().equals(formaId)
                                )
                                .findFirst()
                                .orElseThrow(()
                                        -> new IllegalArgumentException(
                                        "Forma de pagamento inválida."
                                )
                                );

                if (forma.getStatus()
                        != StatusFormaPagamento.CONFIRMADA) {

                    throw new IllegalStateException(
                            "Apenas formas confirmadas podem "
                            + "gerar devolução."
                    );
                }

                PendenciaNegociacao pendencia
                        = new PendenciaNegociacao();

                pendencia.setTipo(
                        TipoPendencia.DEVOLUCAO_CLIENTE
                );

                pendencia.setStatus(
                        StatusPendencia.PENDENTE
                );

                pendencia.setDataCriacao(
                        LocalDateTime.now()
                );

                pendencia.setFormaPagamento(forma);
                pendencia.setValor(forma.getValor());

                pendencia.setDescricao(
                        "Devolver "
                        + forma.getTipo()
                        + " ao comprador "
                        + negociacao.getCliente().getNome()
                        + "."
                );

                negociacao.adicionarPendencia(pendencia);

                pendenciaNegociacaoRepository.saveAndFlush(pendencia);

                pendenciasCriadas.add(pendencia);
            }
        }

        negociacao.setStatus(StatusNegociacao.CANCELADA);
        negociacao.setDataEncerramento(LocalDateTime.now());
        negociacao.setMotivoCancelamento(
                motivoCancelamento.trim()
        );

        Veiculo veiculo = negociacao.getVeiculo();
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);

        negociacaoRepository.save(negociacao);
        veiculoRepository.save(veiculo);

        List<Usuario> administradores
                = usuarioRepository.findByPerfilIn(
                        List.of("DONO", "GERENTE")
                );

        for (PendenciaNegociacao pendencia : pendenciasCriadas) {

            // Notifica o vendedor responsável pela negociação
            notificacaoService.criarNotificacao(
                    negociacao.getVendedor(),
                    TipoNotificacao.PENDENCIA_CRIADA,
                    "Pendência criada",
                    pendencia.getDescricao(),
                    "/negociacoes/" + negociacao.getId(),
                    pendencia.getId()
            );

            // Notifica DONO e GERENTE
            for (Usuario administrador : administradores) {

                if (!administrador.getId()
                        .equals(negociacao.getVendedor().getId())) {

                    notificacaoService.criarNotificacao(
                            administrador,
                            TipoNotificacao.PENDENCIA_CRIADA,
                            "Pendência criada",
                            pendencia.getDescricao(),
                            "/negociacoes/" + negociacao.getId(),
                            pendencia.getId()
                    );
                }
            }
        }

        // Notifica DONO e GERENTE sobre o cancelamento da negociação
        for (Usuario administrador : administradores) {

            notificacaoService.criarNotificacao(
                    administrador,
                    TipoNotificacao.NEGOCIACAO_CANCELADA,
                    "Negociação cancelada",
                    "A negociação #" + negociacao.getId()
                    + " com "
                    + negociacao.getCliente().getNome()
                    + " foi cancelada por "
                    + usuarioLogado.getNome() + ".",
                    null,
                    negociacao.getId()
            );
        }

        return "redirect:/negociacoes";
    }

    private void validarComposicaoPagamento(
            Negociacao negociacao) {

        if (negociacao.getValorProposto() == null
                || negociacao.getValorProposto() <= 0) {

            throw new IllegalArgumentException(
                    "Informe um valor proposto válido."
            );
        }

        if (negociacao.getFormasPagamento() == null
                || negociacao.getFormasPagamento().isEmpty()) {

            throw new IllegalArgumentException(
                    "Informe pelo menos uma forma de pagamento."
            );
        }

        double totalFormas = 0.0;

        for (FormaPagamentoNegociacao forma
                : negociacao.getFormasPagamento()) {

            if (forma.getValor() == null
                    || forma.getValor() <= 0) {

                throw new IllegalArgumentException(
                        "Informe um valor válido "
                        + "para todas as formas de pagamento."
                );
            }

            totalFormas += forma.getValor();
        }

        double diferenca = Math.abs(
                negociacao.getValorProposto() - totalFormas
        );

        if (diferenca >= 0.01) {
            throw new IllegalArgumentException(
                    "O total das formas de pagamento "
                    + "deve ser igual ao valor proposto."
            );
        }
    }

    @PostMapping("/negociacoes/pendencias/{id}/resolver")
    public String resolverPendencia(
            @PathVariable Long id,
            HttpSession session) {

        Usuario usuarioLogado
                = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        PendenciaNegociacao pendencia
                = pendenciaNegociacaoRepository.findById(id).orElse(null);

        if (pendencia == null) {
            return "redirect:/negociacoes";
        }

        pendencia.setStatus(StatusPendencia.RESOLVIDA);
        pendencia.setDataResolucao(LocalDateTime.now());
        pendencia.setResolvidaPor(usuarioLogado);

        pendenciaNegociacaoRepository.save(pendencia);

        // Encerra a ação da notificação de pendência criada
        notificacaoService.resolverNotificacao(
                TipoNotificacao.PENDENCIA_CRIADA,
                pendencia.getId(),
                usuarioLogado.getId()
        );

// Notifica o vendedor responsável
        notificacaoService.criarNotificacao(
                pendencia.getNegociacao().getVendedor(),
                TipoNotificacao.PENDENCIA_RESOLVIDA,
                "Pendência resolvida",
                "A pendência \"" + pendencia.getDescricao()
                + "\" foi resolvida por "
                + usuarioLogado.getNome() + ".",
                null,
                pendencia.getId()
        );

        return "redirect:/negociacoes/"
                + pendencia.getNegociacao().getId();
    }
}
