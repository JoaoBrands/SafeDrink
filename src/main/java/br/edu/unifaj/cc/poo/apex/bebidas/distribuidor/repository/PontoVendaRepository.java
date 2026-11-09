package br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.repository;

import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.PontoVenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoVendaRepository extends JpaRepository<PontoVenda, Integer> {

    List<PontoVenda> findByNomeContainingIgnoreCase(String nome);

    List<PontoVenda> findByTipo(String tipo);
}
