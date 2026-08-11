package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Negociacao;
import br.com.urias.cadastro_veiculos.model.StatusNegociacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegociacaoRepository
        extends JpaRepository<Negociacao, Long> {

    List<Negociacao> findByStatusOrderByDataInicioDesc(
            StatusNegociacao status
    );

    List<Negociacao> findByVendedorIdOrderByDataInicioDesc(
            Long vendedorId
    );
}