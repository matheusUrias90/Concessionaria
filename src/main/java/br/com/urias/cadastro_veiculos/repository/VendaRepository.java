package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Venda;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findTop5ByOrderByIdDesc();

    Page<Venda> findByClienteNomeContainingIgnoreCaseOrVeiculoNomeContainingIgnoreCaseOrVendedorNomeContainingIgnoreCase(
            String cliente,
            String veiculo,
            String vendedor,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(v.valor), 0) FROM Venda v")
    Double somarFaturamento();

    List<Venda> findByClienteIdOrderByIdDesc(Long clienteId);

    List<Venda> findByVendedorIdOrderByIdDesc(Long vendedorId);

    List<Venda> findByVendedorIdOrderByDataVendaDesc(Long vendedorId);

    Page<Venda> findByVendedorIdOrderByDataVendaDesc(
            Long vendedorId,
            Pageable pageable
    );

    Optional<Venda> findByNegociacaoId(Long negociacaoId);

    @Query("""
    SELECT v FROM Venda v
    WHERE (:inicio IS NULL OR v.dataVenda >= :inicio)
      AND (:fim IS NULL OR v.dataVenda <= :fim)
    """)
    List<Venda> buscarVendasPorPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("""
    SELECT v FROM Venda v
    WHERE (:inicio IS NULL OR v.dataVenda >= :inicio)
      AND (:fim IS NULL OR v.dataVenda <= :fim)
      AND (:vendedorId IS NULL OR v.vendedor.id = :vendedorId)
    ORDER BY v.dataVenda ASC, v.id ASC
    """)
    List<Venda> buscarVendasRelatorio(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            @Param("vendedorId") Long vendedorId
    );
}
