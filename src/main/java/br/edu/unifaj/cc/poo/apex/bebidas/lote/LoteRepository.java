package br.edu.unifaj.cc.poo.apex.bebidas.lote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoteRepository extends JpaRepository<Lote, Integer> {

    List<Lote> findByNumeroLoteContainingIgnoreCase(String numeroLote);

    Optional<Lote> findByNumeroLoteIgnoreCase(String numeroLote);

    List<Lote> findBySuspeito(Boolean suspeito);

    long countBySuspeito(Boolean suspeito);

    long countByFabricanteId(Integer fabricanteId);

    long countByFabricanteIdAndSuspeito(Integer fabricanteId, Boolean suspeito);

    /**
     * Resolve a chave que o consumidor tem em mãos: o ID numérico do lote ou o
     * número impresso no rótulo (ex.: LOTE-2024-002). Vazio quando não existe.
     */
    default Optional<Lote> buscarPorChave(String chave) {
        if (chave == null || chave.isBlank()) {
            return Optional.empty();
        }
        String limpo = chave.trim();
        if (limpo.matches("\\d{1,9}")) {
            Optional<Lote> porId = findById(Integer.valueOf(limpo));
            if (porId.isPresent()) {
                return porId;
            }
        }
        return findByNumeroLoteIgnoreCase(limpo);
    }
}
