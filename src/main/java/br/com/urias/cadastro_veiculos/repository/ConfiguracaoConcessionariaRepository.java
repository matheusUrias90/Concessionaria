package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.ConfiguracaoConcessionaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoConcessionariaRepository
        extends JpaRepository<ConfiguracaoConcessionaria, Long> {

}