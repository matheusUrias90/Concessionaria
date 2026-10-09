package br.com.urias.cadastro_veiculos.service;

import br.com.urias.cadastro_veiculos.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;
import br.com.urias.cadastro_veiculos.model.Notificacao;
import br.com.urias.cadastro_veiculos.model.TipoNotificacao;
import br.com.urias.cadastro_veiculos.model.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public void criarNotificacao(
            Usuario usuario,
            TipoNotificacao tipo,
            String titulo,
            String mensagem,
            String link,
            Long referenciaId) {

        Notificacao notificacao = new Notificacao();

        notificacao.setUsuario(usuario);
        notificacao.setTipo(tipo);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setDataCriacao(LocalDateTime.now());
        notificacao.setLida(false);
        notificacao.setLink(link);
        notificacao.setReferenciaId(referenciaId);

        notificacaoRepository.save(notificacao);
    }

    public List<Notificacao> buscarPorUsuario(Long usuarioId) {

        return notificacaoRepository
                .findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);
    }

    public void marcarComoLida(Long notificacaoId, Long usuarioId) {

        Notificacao notificacao
                = notificacaoRepository.findById(notificacaoId).orElse(null);

        if (notificacao != null
                && notificacao.getUsuario().getId().equals(usuarioId)) {

            notificacao.setLida(true);
            notificacaoRepository.save(notificacao);
        }
    }

    public long contarNaoLidas(Long usuarioId) {
        return notificacaoRepository
                .countByUsuarioIdAndLidaFalse(usuarioId);
    }

    public void removerLinkPorReferencia(
            TipoNotificacao tipo,
            Long referenciaId) {

        List<Notificacao> notificacoes
                = notificacaoRepository
                        .findByTipoAndReferenciaId(
                                tipo,
                                referenciaId
                        );

        for (Notificacao notificacao : notificacoes) {
            notificacao.setLink(null);
        }

        notificacaoRepository.saveAll(notificacoes);
    }

    public void resolverNotificacao(
            TipoNotificacao tipo,
            Long referenciaId,
            Long usuarioId) {

        List<Notificacao> notificacoes
                = notificacaoRepository
                        .findByTipoAndReferenciaId(
                                tipo,
                                referenciaId
                        );

        for (Notificacao notificacao : notificacoes) {

            // A ação já foi resolvida, então ninguém mais pode acessá-la
            notificacao.setLink(null);

            // Somente quem resolveu terá a notificação marcada como lida
            if (notificacao.getUsuario().getId().equals(usuarioId)) {
                notificacao.setLida(true);
            }
        }

        notificacaoRepository.saveAll(notificacoes);
    }

    public void marcarTodasComoLidas(Long usuarioId) {

        List<Notificacao> notificacoes
                = notificacaoRepository
                        .findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);

        for (Notificacao notificacao : notificacoes) {

            if (!Boolean.TRUE.equals(notificacao.getLida())) {
                notificacao.setLida(true);
            }
        }

        notificacaoRepository.saveAll(notificacoes);
    }

    // =====================================================
       // BUSCAR NOTIFICAÇÕES COM FILTROS E PAGINAÇÃO
    // =====================================================
    public Page<Notificacao> buscarComFiltros(
            Long usuarioId,
            Boolean lida,
            TipoNotificacao tipo,
            int pagina) {

        Pageable pageable = PageRequest.of(
                Math.max(pagina, 0),
                10
        );

        return notificacaoRepository.buscarComFiltros(
                usuarioId,
                lida,
                tipo,
                pageable
        );
    }
}
