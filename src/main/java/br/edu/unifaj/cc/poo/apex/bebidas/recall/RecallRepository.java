package br.edu.unifaj.cc.poo.apex.bebidas.recall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecallRepository extends JpaRepository<Recall, Integer> {

    List<Recall> findByStatus(String status);

    // @Query explícita pelo mesmo motivo de GarrafaRepository: Recall expõe
    // getLoteId() (auxiliar), o que confunde a derivação "findByLoteId".
    @Query("SELECT r FROM Recall r WHERE r.lote.id = :loteId")
    List<Recall> findByLoteId(@Param("loteId") Integer loteId);
}
