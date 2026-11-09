package br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository;

import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fiscal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FiscalRepository extends JpaRepository<Fiscal, Integer> {

    List<Fiscal> findByNomeContainingIgnoreCase(String nome);
}
