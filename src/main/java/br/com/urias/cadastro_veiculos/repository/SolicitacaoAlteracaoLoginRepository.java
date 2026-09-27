package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.SolicitacaoAlteracaoLogin;
import br.com.urias.cadastro_veiculos.model.StatusSolicitacaoLogin;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoAlteracaoLoginRepository
        extends JpaRepository<SolicitacaoAlteracaoLogin, Long> {

    boolean existsByStatus(StatusSolicitacaoLogin status);
    
    List<SolicitacaoAlteracaoLogin>
            findByStatusOrderByDataSolicitacaoDesc(
                    StatusSolicitacaoLogin status
            );

    boolean existsByUsuarioIdAndStatus(
            Long usuarioId,
            StatusSolicitacaoLogin status
    );
}
