package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Repasse;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepasseRepository extends JpaRepository<Repasse, Long> {

    Optional<Repasse> findByVendaId(Long vendaId);
}