package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.PendenciaNegociacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendenciaNegociacaoRepository
        extends JpaRepository<PendenciaNegociacao, Long> {

    List<PendenciaNegociacao> findByNegociacaoId(Long negociacaoId);
}
