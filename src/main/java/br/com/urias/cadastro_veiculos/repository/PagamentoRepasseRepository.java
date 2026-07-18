package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.PagamentoRepasse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepasseRepository
        extends JpaRepository<PagamentoRepasse, Long> {

    List<PagamentoRepasse> findByRepasseIdOrderByDataPagamentoDesc(Long repasseId);
}