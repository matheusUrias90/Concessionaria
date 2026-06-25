package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository
        extends JpaRepository<Pagamento, Long> {

}