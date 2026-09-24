package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.ItemChecklistEntrega;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemChecklistEntregaRepository
        extends JpaRepository<ItemChecklistEntrega, Long> {
}