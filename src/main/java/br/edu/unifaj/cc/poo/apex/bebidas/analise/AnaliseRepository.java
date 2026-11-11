package br.edu.unifaj.cc.poo.apex.bebidas.analise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnaliseRepository extends JpaRepository<Analise, Integer> {

    List<Analise> findByResultado(String resultado);

    // @Query explícita: Analise expõe getFiscalId()/getDenunciaId() (auxiliares),
    // o que quebra a derivação automática "findByFiscalId"/"findByDenunciaId".
    @Query("SELECT a FROM Analise a WHERE a.fiscal.id = :fiscalId")
    List<Analise> findByFiscalId(@Param("fiscalId") Integer fiscalId);

    @Query("SELECT a FROM Analise a WHERE a.denuncia.id = :denunciaId")
    Analise findByDenunciaId(@Param("denunciaId") Integer denunciaId);
}
