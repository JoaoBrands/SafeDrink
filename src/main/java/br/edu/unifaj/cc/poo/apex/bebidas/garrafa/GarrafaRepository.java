package br.edu.unifaj.cc.poo.apex.bebidas.garrafa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GarrafaRepository extends JpaRepository<Garrafa, Integer> {

    Garrafa findByCodigoBarras(String codigoBarras);

    List<Garrafa> findByStatus(String status);

    // IMPORTANTE: as consultas abaixo usam @Query explícita.
    // Garrafa expõe um getLoteId() (auxiliar, não persistido). Com "findByLoteId"
    // o Spring Data entende "loteId" como um atributo da entidade e o Hibernate
    // falha com UnknownPathException. O JPQL com "g.lote.id" é inequívoco.
    @Query("SELECT g FROM Garrafa g WHERE g.lote.id = :loteId")
    List<Garrafa> findByLoteId(@Param("loteId") Integer loteId);

    @Query("SELECT COUNT(g) FROM Garrafa g WHERE g.lote.id = :loteId")
    long countByLoteId(@Param("loteId") Integer loteId);

    @Query("SELECT COUNT(g) FROM Garrafa g WHERE g.lote.id = :loteId AND g.status = :status")
    long countByLoteIdAndStatus(@Param("loteId") Integer loteId, @Param("status") String status);
}
