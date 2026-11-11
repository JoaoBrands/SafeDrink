package br.edu.unifaj.cc.poo.apex.bebidas.aviso;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.alerta.Alerta;
import br.edu.unifaj.cc.poo.apex.bebidas.alerta.AlertaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.Lote;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fabricante;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.Recall;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.RecallRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Mural público de avisos ao consumidor.
 *
 * É a leitura que o aplicativo do celular consome: recalls e alertas em uma
 * lista única, do mais recente para o mais antigo.
 */
@RestController
@RequestMapping("/api/bebidas/aviso")
public class AvisoController {

    private final RecallRepository recallRepository;
    private final AlertaRepository alertaRepository;

    public AvisoController(RecallRepository recallRepository, AlertaRepository alertaRepository) {
        this.recallRepository = recallRepository;
        this.alertaRepository = alertaRepository;
    }

    @GetMapping
    public Retorno listar() {
        try {
            List<Aviso> avisos = new ArrayList<>();

            for (Recall recall : recallRepository.findAll()) {
                avisos.add(deRecall(recall));
            }
            for (Alerta alerta : alertaRepository.findAll()) {
                avisos.add(deAlerta(alerta));
            }

            avisos.sort(Comparator.comparing(Aviso::getData,
                    Comparator.nullsLast(Comparator.reverseOrder())));

            return new Retorno(avisos);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    // ------------------------------------------------------------------ montagem

    private Aviso deRecall(Recall recall) {
        Aviso aviso = new Aviso();
        aviso.setTipo("RECALL");
        aviso.setData(recall.getDataInicio());
        aviso.setStatus(recall.getStatus());
        aviso.setDescricao(recall.getMotivo());
        aviso.setNivel(emAberto(recall) ? "ALTO" : "ENCERRADO");

        Lote lote = recall.getLote();
        if (lote != null) {
            aplicarLote(aviso, lote);
            aviso.setTitulo(lote.getNumeroLote() != null
                    ? "Recall do lote " + lote.getNumeroLote()
                    : "Recall de lote");
        } else {
            aviso.setTitulo("Recall de lote");
        }
        return aviso;
    }

    private Aviso deAlerta(Alerta alerta) {
        Aviso aviso = new Aviso();
        aviso.setTipo("ALERTA");
        aviso.setNivel(alerta.getNivel());
        aviso.setData(alerta.getData());
        aviso.setRegiao(alerta.getRegiao());
        aviso.setTitulo("Aviso sanitário");

        Recall recall = alerta.getRecall();
        if (recall != null) {
            aviso.setDescricao(recall.getMotivo());
            aviso.setStatus(recall.getStatus());
            if (recall.getLote() != null) {
                aplicarLote(aviso, recall.getLote());
            }
        }
        return aviso;
    }

    private void aplicarLote(Aviso aviso, Lote lote) {
        aviso.setLoteNumero(lote.getNumeroLote());
        Fabricante fabricante = lote.getFabricante();
        if (fabricante != null) {
            aviso.setFabricante(fabricante.getRazaoSocial() != null
                    ? fabricante.getRazaoSocial()
                    : fabricante.getNome());
        }
    }

    /** Um recall deixa de aparecer como grave quando foi encerrado. */
    private boolean emAberto(Recall recall) {
        String status = recall.getStatus();
        if (status == null) {
            return true;
        }
        String s = status.toUpperCase(Locale.ROOT);
        return !(s.contains("ENCERR") || s.contains("CONCLU")
                || s.contains("CANCEL") || s.contains("FINALIZ"));
    }
}
