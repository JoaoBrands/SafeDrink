package br.edu.unifaj.cc.poo.apex.bebidas.relatorio;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.alerta.AlertaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.analise.Analise;
import br.edu.unifaj.cc.poo.apex.bebidas.analise.AnaliseRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.DestiladoRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.FermentadoRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.VinhoRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.denuncia.Denuncia;
import br.edu.unifaj.cc.poo.apex.bebidas.denuncia.DenunciaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.repository.DistribuidorRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.repository.PontoVendaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.garrafa.GarrafaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.Lote;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.LoteRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.ConsumidorRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.FabricanteRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.FiscalRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.RecallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bebidas/relatorio")
public class RelatorioController {

    @Autowired
    LoteRepository loteRepository;

    @Autowired
    GarrafaRepository garrafaRepository;

    @Autowired
    FabricanteRepository fabricanteRepository;

    @Autowired
    ConsumidorRepository consumidorRepository;

    @Autowired
    FiscalRepository fiscalRepository;

    @Autowired
    DestiladoRepository destiladoRepository;

    @Autowired
    FermentadoRepository fermentadoRepository;

    @Autowired
    VinhoRepository vinhoRepository;

    @Autowired
    DistribuidorRepository distribuidorRepository;

    @Autowired
    PontoVendaRepository pontoVendaRepository;

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    AnaliseRepository analiseRepository;

    @Autowired
    RecallRepository recallRepository;

    @Autowired
    AlertaRepository alertaRepository;

    /** GET /api/bebidas/relatorio/lote/{id}/total-garrafas
      Retorna o total de garrafas que pertencem a um lote.*/
    @GetMapping("/lote/{id}/total-garrafas")
    public Retorno totalGarrafasLote(@PathVariable Integer id) {
        try {
            Lote lote = loteRepository.findById(id).orElse(null);
            if (lote == null) {
                return new Retorno("Lote não encontrado: " + id);
            }
            long totalGarrafas = garrafaRepository.countByLoteId(id);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("loteId", id);
            resultado.put("numeroLote", lote.getNumeroLote());
            resultado.put("totalGarrafas", totalGarrafas);

            return new Retorno(resultado);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /**GET /api/bebidas/relatorio/lote/{id}/percentual-falsificado
      Retorna o percentual de garrafas falsificadas em um lote.*/
    @GetMapping("/lote/{id}/percentual-falsificado")
    public Retorno percentualFalsificadoLote(@PathVariable Integer id) {
        try {
            Lote lote = loteRepository.findById(id).orElse(null);
            if (lote == null) {
                return new Retorno("Lote não encontrado: " + id);
            }

            long totalGarrafas = garrafaRepository.countByLoteId(id);
            long totalFalsificadas = garrafaRepository.countByLoteIdAndStatus(id, "FALSIFICADA");

            double percentual = 0.0;
            if (totalGarrafas > 0) {
                percentual = (totalFalsificadas * 100.0) / totalGarrafas;
            }

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("loteId", id);
            resultado.put("numeroLote", lote.getNumeroLote());
            resultado.put("totalGarrafas", totalGarrafas);
            resultado.put("garrafasFalsificadas", totalFalsificadas);
            resultado.put("percentualFalsificado", percentual);

            return new Retorno(resultado);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /**GET /api/bebidas/relatorio/fabricante/{id}/taxa-reincidencia
      Retorna a taxa de reincidência do fabricante.*/
    @GetMapping("/fabricante/{id}/taxa-reincidencia")
    public Retorno taxaReincidenciaFabricante(@PathVariable Integer id) {
        try {
            if (!fabricanteRepository.existsById(id)) {
                return new Retorno("Fabricante não encontrado: " + id);
            }

            long totalLotes = loteRepository.count();
            long lotesSuspeitos = loteRepository.countByFabricanteIdAndSuspeito(id, true);
            long lotesFabricante = loteRepository.countByFabricanteId(id);

            double taxa = 0.0;
            if (lotesFabricante > 0) {
                taxa = (lotesSuspeitos * 100.0) / lotesFabricante;
            }

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("fabricanteId", id);
            resultado.put("totalLotesFabricante", lotesFabricante);
            resultado.put("lotesSuspeitos", lotesSuspeitos);
            resultado.put("totalLotesSistema", totalLotes);
            resultado.put("taxaReincidencia", taxa);

            return new Retorno(resultado);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /**GET /api/bebidas/relatorio/denuncia/{id}/tempo-resolucao
     * Retorna quantos dias entre a denúncia e a análise.*/
    @GetMapping("/denuncia/{id}/tempo-resolucao")
    public Retorno tempoResolucaoDenuncia(@PathVariable Integer id) {
        try {
            Denuncia denuncia = denunciaRepository.findById(id).orElse(null);
            if (denuncia == null) {
                return new Retorno("Denúncia não encontrada: " + id);
            }

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("denunciaId", id);
            resultado.put("dataDenuncia", denuncia.getData());

            Analise analise = analiseRepository.findByDenunciaId(id);

            if (analise == null) {
                resultado.put("status", "SEM_ANALISE");
                resultado.put("dias", null);
                return new Retorno(resultado);
            }

            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataDenuncia = LocalDate.parse(denuncia.getData(), formato);
            LocalDate dataAnalise = LocalDate.parse(analise.getData(), formato);

            long dias = ChronoUnit.DAYS.between(dataDenuncia, dataAnalise);

            resultado.put("dataAnalise", analise.getData());
            resultado.put("dias", dias);
            resultado.put("resultadoAnalise", analise.getResultado());

            return new Retorno(resultado);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /**GET /api/bebidas/relatorio/recall/{id}/total-alertas
     Retorna quantos alertas foram emitidos por um recall.*/
    @GetMapping("/recall/{id}/total-alertas")
    public Retorno totalAlertasRecall(@PathVariable Integer id) {
        try {
            if (!recallRepository.existsById(id)) {
                return new Retorno("Recall não encontrado: " + id);
            }

            long totalAlertas = alertaRepository.countByRecallId(id);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("recallId", id);
            resultado.put("totalAlertas", totalAlertas);

            return new Retorno(resultado);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /**GET /api/bebidas/relatorio/estatisticas
      Estatísticas gerais do sistema.*/
    @GetMapping("/estatisticas")
    public Retorno estatisticas() {
        try {
            Map<String, Object> resultado = new HashMap<>();

            resultado.put("totalConsumidores", consumidorRepository.count());
            resultado.put("totalFiscais", fiscalRepository.count());
            resultado.put("totalFabricantes", fabricanteRepository.count());
            resultado.put("totalDestilados", destiladoRepository.count());
            resultado.put("totalFermentados", fermentadoRepository.count());
            resultado.put("totalVinhos", vinhoRepository.count());
            resultado.put("totalDistribuidores", distribuidorRepository.count());
            resultado.put("totalPontosVenda", pontoVendaRepository.count());
            resultado.put("totalLotes", loteRepository.count());
            resultado.put("totalGarrafas", garrafaRepository.count());
            resultado.put("totalDenuncias", denunciaRepository.count());
            resultado.put("totalAnalises", analiseRepository.count());
            resultado.put("totalRecalls", recallRepository.count());
            resultado.put("totalAlertas", alertaRepository.count());

            resultado.put("lotesSuspeitos", loteRepository.countBySuspeito(true));
            resultado.put("garrafasFalsificadas", garrafaRepository.findByStatus("FALSIFICADA").size());

            return new Retorno(resultado);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}