package br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository;

import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Fermentado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FermentadoRepository extends JpaRepository<Fermentado, Integer> {

    List<Fermentado> findByNomeContainingIgnoreCase(String nome);
}