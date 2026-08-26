package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Negociacao;
import br.com.urias.cadastro_veiculos.model.StatusNegociacao;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NegociacaoRepository
        extends JpaRepository<Negociacao, Long> {

    List<Negociacao> findByStatusAndVendedorIdOrderByDataInicioDesc(
            StatusNegociacao status,
            Long vendedorId
    );

    List<Negociacao> findByStatusOrderByDataInicioDesc(
            StatusNegociacao status
    );

    List<Negociacao> findByVendedorIdOrderByDataInicioDesc(
            Long vendedorId
    );

    @Query("""
       SELECT n
       FROM Negociacao n
       WHERE (:status IS NULL OR n.status = :status)
         AND (:vendedorId IS NULL OR n.vendedor.id = :vendedorId)
         AND (
             :busca = ''
             OR LOWER(n.cliente.nome)
                LIKE LOWER(CONCAT('%', :busca, '%'))
             OR LOWER(n.veiculo.nome)
                LIKE LOWER(CONCAT('%', :busca, '%'))
             OR LOWER(n.vendedor.nome)
                LIKE LOWER(CONCAT('%', :busca, '%'))
         )
       ORDER BY n.dataInicio DESC
       """)
    Page<Negociacao> filtrarNegociacoes(
            @Param("status") StatusNegociacao status,
            @Param("busca") String busca,
            @Param("vendedorId") Long vendedorId,
            Pageable pageable
    );
}