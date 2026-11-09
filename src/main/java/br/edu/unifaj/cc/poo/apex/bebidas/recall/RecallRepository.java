package br.edu.unifaj.cc.poo.apex.bebidas.recall;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecallRepository extends JpaRepository<Recall, Integer> {

    List<Recall> findByStatus(String status);

    List<Recall> findByLoteId(Integer loteId);
}