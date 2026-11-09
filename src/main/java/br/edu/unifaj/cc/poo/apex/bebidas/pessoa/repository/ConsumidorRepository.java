package br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository;

import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Consumidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumidorRepository extends JpaRepository<Consumidor, Integer> {

    List<Consumidor> findByNomeContainingIgnoreCase(String nome);
}
