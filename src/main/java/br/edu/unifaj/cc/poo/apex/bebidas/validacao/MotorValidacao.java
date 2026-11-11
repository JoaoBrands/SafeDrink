package br.edu.unifaj.cc.poo.apex.bebidas.validacao;

import br.edu.unifaj.cc.poo.apex.bebidas.analise.Analise;
import br.edu.unifaj.cc.poo.apex.bebidas.analise.AnaliseRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Destilado;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Fermentado;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Vinho;
import br.edu.unifaj.cc.poo.apex.bebidas.denuncia.Denuncia;
import br.edu.unifaj.cc.poo.apex.bebidas.denuncia.DenunciaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.Distribuidor;
import br.edu.unifaj.cc.poo.apex.bebidas.garrafa.Garrafa;
import br.edu.unifaj.cc.poo.apex.bebidas.garrafa.GarrafaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.Lote;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.LoteRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fabricante;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.Recall;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.RecallRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Motor de validação de autenticidade.
 *
 * Reúne as evidências que já existem no banco (fabricante, recall, laudo
 * pericial, status das garrafas e validade) e devolve um veredito com os
 * motivos. Antes, a página de validação olhava apenas o campo booleano
 * "suspeito" do lote.
 */
@Service
public class MotorValidacao {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final LoteRepository loteRepository;
    private final GarrafaRepository garrafaRepository;
    private final RecallRepository recallRepository;
    private final DenunciaRepository denunciaRepository;
    private final AnaliseRepository analiseRepository;

    public MotorValidacao(LoteRepository loteRepository,
                          GarrafaRepository garrafaRepository,
                          RecallRepository recallRepository,
                          DenunciaRepository denunciaRepository,
                          AnaliseRepository analiseRepository) {
        this.loteRepository = loteRepository;
        this.garrafaRepository = garrafaRepository;
        this.recallRepository = recallRepository;
        this.denunciaRepository = denunciaRepository;
        this.analiseRepository = analiseRepository;
    }

    // ------------------------------------------------------------------ entradas

    /**
     * Aceita tanto o ID numérico (ex: "1") quanto o número do lote impresso no
     * rótulo (ex: "LOT-2026-FAL02"), que é o que o consumidor consegue digitar.
     */
    public ResultadoValidacao validarPorChave(String chave) {
        return validarLote(buscarLote(chave));
    }

    public ResultadoValidacao validarPorCodigoBarras(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.isBlank()) {
            return garrafaNaoEncontrada();
        }
        return validarGarrafa(garrafaRepository.findByCodigoBarras(codigoBarras.trim()));
    }

    // ------------------------------------------------------------------ lote

    public ResultadoValidacao validarLote(Lote lote) {
        if (lote == null) {
            ResultadoValidacao r = new ResultadoValidacao();
            r.setNivel(ResultadoValidacao.NAO_ENCONTRADO);
            r.setTitulo("Registro inexistente");
            r.setMensagem("Este lote não consta na base oficial do SafeDrink. "
                    + "A bebida pode não estar registrada ou ser uma falsificação.");
            return r;
        }

        ResultadoValidacao r = new ResultadoValidacao();
        List<String> suspeitos = new ArrayList<>();
        List<String> atencao = new ArrayList<>();

        preencherLote(r, lote);
        avaliarLote(lote, r, suspeitos, atencao);

        return concluir(r, suspeitos, atencao);
    }

    // ------------------------------------------------------------------ garrafa

    public ResultadoValidacao validarGarrafa(Garrafa garrafa) {
        if (garrafa == null) {
            return garrafaNaoEncontrada();
        }

        int leituras = garrafa.getContadorVerificacoes() + 1;
        garrafa.setContadorVerificacoes(leituras);
        garrafaRepository.save(garrafa);

        ResultadoValidacao r = new ResultadoValidacao();
        List<String> suspeitos = new ArrayList<>();
        List<String> atencao = new ArrayList<>();

        preencherGarrafa(r, garrafa);

        if (leituras > 3) {
            suspeitos.add("Alerta de clonagem: Este selo único já foi consultado "
                    + leituras + " vezes no sistema.");
        }

        if (garrafa.getLote() != null) {
            preencherLote(r, garrafa.getLote());
            avaliarLote(garrafa.getLote(), r, suspeitos, atencao);
        } else {
            atencao.add("Garrafa registrada sem lote vinculado.");
        }

        if ("FALSIFICADA".equalsIgnoreCase(texto(garrafa.getStatus()))) {
            suspeitos.add("Este selo está registrado como FALSIFICADO pela fiscalização.");
        } else if ("SUSPEITA".equalsIgnoreCase(texto(garrafa.getStatus()))) {
            atencao.add("Esta garrafa foi sinalizada como suspeita pela fiscalização e ainda aguarda conclusão.");
        }

        if (texto(garrafa.getSeloFiscal()) == null) {
            atencao.add("Garrafa registrada sem selo fiscal.");
        }

        return concluir(r, suspeitos, atencao);
    }

    // ------------------------------------------------------------------ regras

    private void avaliarLote(Lote lote, ResultadoValidacao r, List<String> suspeitos, List<String> atencao) {
        Fabricante fabricante = lote.getFabricante();

        // 1. Fabricante sem registro regular no MAPA
        if (fabricante == null) {
            atencao.add("Lote sem fabricante identificado no registro.");
        } else if (Boolean.FALSE.equals(fabricante.getRegular())) {
            suspeitos.add("Fabricante " + nomeFabricante(fabricante) + " não possui registro regular no MAPA.");
        }

        // 2. Recall (recolhimento) aberto para este lote
        List<Recall> recalls = recallRepository.findByLoteId(lote.getId());
        r.setTemRecall(!recalls.isEmpty());

        Recall recallAberto = null;
        for (Recall recall : recalls) {
            if (recallAberto == null && recallEmAberto(recall)) {
                recallAberto = recall;
            }
        }
        if (recallAberto != null) {
            suspeitos.add("Recall aberto para este lote: "
                    + texto(recallAberto.getMotivo(), "motivo não informado") + ".");
        }

        // 3. Laudo pericial concluindo fraude
        String laudo = laudoDeFraude(lote);
        if (laudo != null) {
            suspeitos.add("Laudo pericial registrado para este lote: " + laudo + ".");
        }

        // 4. Marcação manual da fiscalização
        if (Boolean.TRUE.equals(lote.getSuspeito())) {
            suspeitos.add("O lote está marcado como suspeito pela fiscalização.");
        }

        // 5. Garrafas do lote já identificadas como falsificadas
        long falsificadas = garrafaRepository.countByLoteIdAndStatus(lote.getId(), "FALSIFICADA");
        long totalGarrafas = garrafaRepository.countByLoteId(lote.getId());
        r.setGarrafasFalsificadas((int) falsificadas);
        r.setTotalGarrafas((int) totalGarrafas);
        if (falsificadas > 0) {
            atencao.add(falsificadas + " garrafa(s) deste lote já foram identificadas como falsificadas.");
        }

        // 6. Validade
        LocalDate validade = lerData(lote.getDataValidade());
        if (validade != null) {
            long dias = ChronoUnit.DAYS.between(LocalDate.now(), validade);
            r.setVencido(dias < 0);
            r.setDiasParaVencer(dias);
            if (dias < 0) {
                atencao.add("Data de validade vencida em " + lote.getDataValidade() + ".");
            }
        }
    }

    /**
     * Procura um laudo pericial que já tenha concluído fraude. Aceita tanto
     * "ADULTERADA" quanto "FALSIFICADA", que são os dois resultados usados
     * pela fiscalização neste domínio.
     */
    private String laudoDeFraude(Lote lote) {
        for (Denuncia denuncia : denunciaRepository.findByLoteOuGarrafaDoLote(lote.getId())) {
            Analise analise = analiseRepository.findByDenunciaId(denuncia.getId());
            if (analise == null) {
                continue;
            }
            String resultado = texto(analise.getResultado());
            if (resultado == null) {
                continue;
            }
            String maiusculo = resultado.toUpperCase(Locale.ROOT);
            if (maiusculo.contains("ADULTERAD") || maiusculo.contains("FALSIFICAD")) {
                return resultado.trim();
            }
        }
        return null;
    }

    /**
     * Um recall está aberto quando não foi encerrado explicitamente e a data
     * final ainda não passou.
     */
    private boolean recallEmAberto(Recall recall) {
        String status = texto(recall.getStatus());
        if (status != null) {
            String s = status.toUpperCase(Locale.ROOT);
            if (s.contains("ENCERR") || s.contains("CONCLU") || s.contains("CANCEL") || s.contains("FINALIZ")) {
                return false;
            }
        }
        LocalDate fim = lerData(recall.getDataFim());
        return fim == null || !fim.isBefore(LocalDate.now());
    }

    // ------------------------------------------------------------------ preenchimento

    private void preencherLote(ResultadoValidacao r, Lote lote) {
        r.setLoteId(lote.getId());
        r.setNumeroLote(lote.getNumeroLote());
        r.setBebida(nomeBebida(lote));
        r.setDataFabricacao(lote.getDataFabricacao());
        r.setDataValidade(lote.getDataValidade());
        r.setQuantidade(lote.getQuantidade());

        Fabricante fabricante = lote.getFabricante();
        if (fabricante != null) {
            r.setFabricante(nomeFabricante(fabricante));
            r.setFabricanteRegular(fabricante.getRegular());
            r.setRegistroMAPA(texto(fabricante.getRegistroMAPA()) == null
                    ? "Sem registro" : fabricante.getRegistroMAPA());
        } else {
            r.setFabricante("Desconhecido");
            r.setRegistroMAPA("Sem registro");
        }

        Distribuidor distribuidor = lote.getDistribuidor();
        r.setDistribuidor(distribuidor == null
                ? "Direto do fabricante"
                : nomeDistribuidor(distribuidor));
    }

    private void preencherGarrafa(ResultadoValidacao r, Garrafa garrafa) {
        r.setCodigoBarras(garrafa.getCodigoBarras());
        r.setSeloFiscal(garrafa.getSeloFiscal());
        r.setStatusGarrafa(garrafa.getStatus());
        r.setPontoVenda(garrafa.getPontoVenda() == null
                ? null : garrafa.getPontoVenda().getNome());
    }

    private ResultadoValidacao concluir(ResultadoValidacao r, List<String> suspeitos, List<String> atencao) {
        r.getMotivos().addAll(suspeitos);
        r.getMotivos().addAll(atencao);

        if (!suspeitos.isEmpty()) {
            r.setNivel(ResultadoValidacao.SUSPEITO);
            r.setTitulo("Produto suspeito — não consuma");
            r.setMensagem("Foram encontradas evidências objetivas de fraude ou risco sanitário "
                    + "para este registro. Não consuma o produto e avise a fiscalização.");
        } else if (!atencao.isEmpty()) {
            r.setNivel(ResultadoValidacao.ATENCAO);
            r.setTitulo("Autenticidade não confirmada");
            r.setMensagem("O registro existe, mas há pontos de atenção que impedem confirmar "
                    + "a autenticidade. Confira os motivos abaixo.");
        } else {
            r.setNivel(ResultadoValidacao.AUTENTICO);
            r.setTitulo("Selo de rastreabilidade conforme");
            r.setMensagem("Registro localizado, fabricante regular, sem recall em aberto, "
                    + "sem laudo de adulteração e dentro da validade.");
        }
        return r;
    }

    private ResultadoValidacao garrafaNaoEncontrada() {
        ResultadoValidacao r = new ResultadoValidacao();
        r.setNivel(ResultadoValidacao.NAO_ENCONTRADO);
        r.setTitulo("Selo não reconhecido");
        r.setMensagem("Este código não consta na base oficial do SafeDrink. "
                + "Pode se tratar de uma garrafa não registrada ou falsificada.");
        return r;
    }

    // ------------------------------------------------------------------ utilitários

    private Lote buscarLote(String chave) {
        // ID numérico ou número do lote impresso no rótulo (regra única no repositório)
        return loteRepository.buscarPorChave(chave).orElse(null);
    }

    private String nomeBebida(Lote lote) {
        Vinho vinho = lote.getVinho();
        if (vinho != null) {
            return "Vinho: " + texto(vinho.getNome(), "");
        }
        Destilado destilado = lote.getDestilado();
        if (destilado != null) {
            return "Destilado: " + texto(destilado.getNome(), "");
        }
        Fermentado fermentado = lote.getFermentado();
        if (fermentado != null) {
            return "Fermentado: " + texto(fermentado.getNome(), "");
        }
        return "Não especificada";
    }

    private String nomeFabricante(Fabricante fabricante) {
        return texto(fabricante.getRazaoSocial(), texto(fabricante.getNome(), "Fabricante sem nome"));
    }

    private String nomeDistribuidor(Distribuidor distribuidor) {
        return texto(distribuidor.getRazaoSocial(), texto(distribuidor.getCnpj(), "Distribuidor sem nome"));
    }

    private static LocalDate lerData(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(data.trim(), FORMATO_DATA);
        } catch (Exception e) {
            return null;
        }
    }

    private static String texto(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor;
    }

    private static String texto(String valor, String padrao) {
        String limpo = texto(valor);
        return limpo == null ? padrao : limpo;
    }
}
