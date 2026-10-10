package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Notificacao;
import br.com.urias.cadastro_veiculos.model.TipoNotificacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);

    long countByUsuarioIdAndLidaFalse(Long usuarioId);

    List<Notificacao> findByTipoAndReferenciaId(
            TipoNotificacao tipo,
            Long referenciaId
    );

    @Query("""
       SELECT n FROM Notificacao n
       WHERE n.usuario.id = :usuarioId
       AND (:lida IS NULL OR n.lida = :lida)
       AND (:tipo IS NULL OR n.tipo = :tipo)
       ORDER BY n.dataCriacao DESC, n.id DESC
       """)
    Page<Notificacao> buscarComFiltros(
            @Param("usuarioId") Long usuarioId,
            @Param("lida") Boolean lida,
            @Param("tipo") TipoNotificacao tipo,
            Pageable pageable
    );

    @Query("""
       SELECT DISTINCT n.tipo
       FROM Notificacao n
       WHERE n.usuario.id = :usuarioId
       ORDER BY n.tipo
       """)
    List<TipoNotificacao> buscarTiposPorUsuario(
            @Param("usuarioId") Long usuarioId
    );
}
