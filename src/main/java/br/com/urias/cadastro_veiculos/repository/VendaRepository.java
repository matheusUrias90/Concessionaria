package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Venda;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}