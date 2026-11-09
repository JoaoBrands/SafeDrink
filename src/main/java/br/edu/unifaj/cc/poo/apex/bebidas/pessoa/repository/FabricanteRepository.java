package br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository;

import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fabricante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FabricanteRepository extends JpaRepository<Fabricante, Integer> {

    List<Fabricante> findByNomeContainingIgnoreCase(String nome);
}
