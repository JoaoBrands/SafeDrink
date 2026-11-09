package br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository;

import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Vinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VinhoRepository extends JpaRepository<Vinho, Integer> {

    List<Vinho> findByNomeContainingIgnoreCase(String nome);
}