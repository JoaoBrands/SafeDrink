package br.edu.unifaj.cc.poo.apex.bebidas.denuncia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, Integer> {

    List<Denuncia> findByStatus(String status);

    List<Denuncia> findByConsumidorId(Integer consumidorId);
}