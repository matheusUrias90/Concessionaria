package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Page<Cliente>findByNomeContainingIgnoreCaseOrCpfContainingIgnoreCaseOrTelefoneContainingIgnoreCase(
            String nome,
            String cpf,
            String telefone,
            Pageable pageable
    );
}