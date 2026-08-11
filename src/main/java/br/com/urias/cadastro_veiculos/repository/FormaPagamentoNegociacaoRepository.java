package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.FormaPagamentoNegociacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormaPagamentoNegociacaoRepository
        extends JpaRepository<FormaPagamentoNegociacao, Long> {

    List<FormaPagamentoNegociacao> findByNegociacaoId(Long negociacaoId);
}