package br.edu.unifaj.cc.poo.apex.bebidas.alerta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Integer> {

    List<Alerta> findByNivel(String nivel);

    List<Alerta> findByRegiao(String regiao);

    long countByRecallId(Integer recallId);
}