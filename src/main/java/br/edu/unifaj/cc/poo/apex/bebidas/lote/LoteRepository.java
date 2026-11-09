package br.edu.unifaj.cc.poo.apex.bebidas.lote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Integer> {

    List<Lote> findByNumeroLoteContainingIgnoreCase(String numeroLote);

    List<Lote> findBySuspeito(Boolean suspeito);

    long countBySuspeito(Boolean suspeito);

    long countByFabricanteId(Integer fabricanteId);

    long countByFabricanteIdAndSuspeito(Integer fabricanteId, Boolean suspeito);
}
