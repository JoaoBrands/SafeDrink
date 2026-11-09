package br.edu.unifaj.cc.poo.apex.bebidas.analise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnaliseRepository extends JpaRepository<Analise, Integer> {

    List<Analise> findByResultado(String resultado);

    List<Analise> findByFiscalId(Integer fiscalId);

    Analise findByDenunciaId(Integer denunciaId);
}