package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.StatusVeiculo;
import br.com.urias.cadastro_veiculos.model.Veiculo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    List<Veiculo> findByStatus(StatusVeiculo status);

    Page<Veiculo> findByStatus(StatusVeiculo status, Pageable pageable);

    long countByStatus(StatusVeiculo status);

    Page<Veiculo> findByStatusAndNomeContainingIgnoreCaseOrStatusAndMarcaContainingIgnoreCaseOrStatusAndPlacaContainingIgnoreCase(
            StatusVeiculo statusNome,
            String nome,
            StatusVeiculo statusMarca,
            String marca,
            StatusVeiculo statusPlaca,
            String placa,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(v.preco), 0) FROM Veiculo v WHERE v.status = 'DISPONIVEL'")
    Double somarEstoqueDisponivel();

    @Query("""
    SELECT COALESCE(SUM(v.preco), 0)
    FROM Veiculo v
    WHERE v.status IN :status
    """)
    Double somarValorEstoque(
            @Param("status") List<StatusVeiculo> status
    );

    Page<Veiculo> findByStatusIn(
            List<StatusVeiculo> status,
            Pageable pageable
    );

    @Query("""
    SELECT v FROM Veiculo v
    WHERE v.status IN (
        br.com.urias.cadastro_veiculos.model.StatusVeiculo.DISPONIVEL,
        br.com.urias.cadastro_veiculos.model.StatusVeiculo.RESERVADO
    )
    AND (
        :busca = ''
        OR LOWER(v.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
        OR LOWER(v.marca) LIKE LOWER(CONCAT('%', :busca, '%'))
        OR LOWER(v.placa) LIKE LOWER(CONCAT('%', :busca, '%'))
    )
    """)
    Page<Veiculo> buscarEstoqueRelatorio(
            @Param("busca") String busca,
            Pageable pageable
    );
}
