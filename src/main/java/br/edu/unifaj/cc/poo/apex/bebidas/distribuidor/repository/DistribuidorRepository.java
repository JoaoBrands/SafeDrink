package br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.repository;

import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.Distribuidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistribuidorRepository extends JpaRepository<Distribuidor, Integer> {

    List<Distribuidor> findByRazaoSocialContainingIgnoreCase(String razaoSocial);

    List<Distribuidor> findByRegiao(String regiao);
}
