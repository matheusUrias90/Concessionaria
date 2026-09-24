package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.ChecklistEntrega;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistEntregaRepository
        extends JpaRepository<ChecklistEntrega, Long> {

    Optional<ChecklistEntrega> findByVeiculoId(Long veiculoId);
}