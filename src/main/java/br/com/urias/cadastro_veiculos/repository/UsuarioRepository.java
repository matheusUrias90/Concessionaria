package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Usuario;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByLoginAndSenha(String login, String senha);

    Usuario findByLogin(String login);
    
    boolean existsByLogin(String login);

    Page<Usuario> findByNomeContainingIgnoreCaseOrLoginContainingIgnoreCase(
            String nome,
            String login,
            Pageable pageable
    );
    
    List<Usuario> findByPerfilIn(List<String> perfis);
}
