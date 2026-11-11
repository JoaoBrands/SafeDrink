package br.edu.unifaj.cc.poo.apex.bebidas.denuncia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DenunciaRepository extends JpaRepository<Denuncia, Integer> {

    List<Denuncia> findByStatus(String status);

    List<Denuncia> findByConsumidorId(Integer consumidorId);

    Optional<Denuncia> findByProtocolo(String protocolo);

    /**
     * Denúncias ligadas a um lote, direta ou indiretamente (pela garrafa
     * reclamada). Os LEFT JOIN explícitos evitam descartar registros que
     * preencheram apenas um dos dois vínculos.
     */
    @Query("SELECT d FROM Denuncia d "
            + "LEFT JOIN d.lote l "
            + "LEFT JOIN d.garrafa g "
            + "LEFT JOIN g.lote gl "
            + "WHERE l.id = :loteId OR gl.id = :loteId")
    List<Denuncia> findByLoteOuGarrafaDoLote(@Param("loteId") Integer loteId);
}
