package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByVendidoFalse();
    
    Page<Veiculo> findByVendidoFalse(Pageable pageable);
    
    long countByVendidoTrue();
    long countByVendidoFalse();
    
    Page<Veiculo>findByNomeContainingIgnoreCaseOrMarcaContainingIgnoreCaseOrPlacaContainingIgnoreCase(
            String nome,
            String marca,
            String placa,
            Pageable pageable
    );
    
    @Query("SELECT COALESCE(SUM(v.preco), 0) FROM Veiculo v WHERE v.vendido = false")
    Double somarEstoqueDisponivel();
}