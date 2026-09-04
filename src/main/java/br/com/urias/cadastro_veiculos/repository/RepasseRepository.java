package br.com.urias.cadastro_veiculos.repository;

import br.com.urias.cadastro_veiculos.model.Repasse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepasseRepository extends JpaRepository<Repasse, Long> {

    Optional<Repasse> findByVendaId(Long vendaId);

    @Query(
            value = """
            SELECT r
            FROM Repasse r
            WHERE COALESCE(
                (
                    SELECT SUM(p.valor)
                    FROM PagamentoRepasse p
                    WHERE p.repasse = r
                ),
                0
            ) >= r.valorDevido
              AND (
                  LOWER(r.venda.veiculo.nomeProprietario)
                      LIKE LOWER(CONCAT('%', :busca, '%'))
                  OR LOWER(r.venda.veiculo.nome)
                      LIKE LOWER(CONCAT('%', :busca, '%'))
                  OR LOWER(r.venda.veiculo.placa)
                      LIKE LOWER(CONCAT('%', :busca, '%'))
              )
            ORDER BY r.id DESC
        """,
            countQuery = """
            SELECT COUNT(r)
            FROM Repasse r
            WHERE COALESCE(
                (
                    SELECT SUM(p.valor)
                    FROM PagamentoRepasse p
                    WHERE p.repasse = r
                ),
                0
            ) >= r.valorDevido
              AND (
                  LOWER(r.venda.veiculo.nomeProprietario)
                      LIKE LOWER(CONCAT('%', :busca, '%'))
                  OR LOWER(r.venda.veiculo.nome)
                      LIKE LOWER(CONCAT('%', :busca, '%'))
                  OR LOWER(r.venda.veiculo.placa)
                      LIKE LOWER(CONCAT('%', :busca, '%'))
              )
        """
    )
    Page<Repasse> buscarFinalizados(
            @Param("busca") String busca,
            Pageable pageable
    );
}
