package br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository;

import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Destilado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestiladoRepository extends JpaRepository<Destilado, Integer> {

    List<Destilado> findByNomeContainingIgnoreCase(String nome);
}