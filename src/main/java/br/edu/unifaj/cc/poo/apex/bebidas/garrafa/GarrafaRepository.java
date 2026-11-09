package br.edu.unifaj.cc.poo.apex.bebidas.garrafa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GarrafaRepository extends JpaRepository<Garrafa, Integer> {

    Garrafa findByCodigoBarras(String codigoBarras);

    List<Garrafa> findByStatus(String status);

    List<Garrafa> findByLoteId(Integer loteId);

    long countByLoteId(Integer loteId);

    long countByLoteIdAndStatus(Integer loteId, String status);
}