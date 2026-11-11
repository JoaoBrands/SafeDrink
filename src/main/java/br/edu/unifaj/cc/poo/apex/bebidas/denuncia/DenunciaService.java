package br.edu.unifaj.cc.poo.apex.bebidas.denuncia;

import br.edu.unifaj.cc.poo.apex.bebidas.analise.Analise;
import br.edu.unifaj.cc.poo.apex.bebidas.analise.AnaliseRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.Lote;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.LoteRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;

/**
 * Regras de negócio da denúncia.
 *
 * Duas responsabilidades:
 *  1. No registro, o SERVIDOR decide a data, o status inicial e o protocolo
 *     (o aplicativo não deveria escolher nada disso).
 *  2. Na consulta pelo protocolo, devolve apenas a visão pública — sem os
 *     dados pessoais de quem denunciou.
 */
@Service
public class DenunciaService {

    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int TAMANHO_PROTOCOLO = 6;
    private static final int TENTATIVAS = 20;
    private static final int LIMITE_DESCRICAO = 255;   // tamanho da coluna
    private static final int LIMITE_CODIGO = 40;

    private final DenunciaRepository denunciaRepository;
    private final AnaliseRepository analiseRepository;
    private final LoteRepository loteRepository;
    private final SecureRandom sorteio = new SecureRandom();

    public DenunciaService(DenunciaRepository denunciaRepository,
                           AnaliseRepository analiseRepository,
                           LoteRepository loteRepository) {
        this.denunciaRepository = denunciaRepository;
        this.analiseRepository = analiseRepository;
        this.loteRepository = loteRepository;
    }

    /**
     * Registra uma denúncia nova.
     *
     * Data, status inicial e protocolo são SEMPRE do servidor: o aplicativo
     * não deve poder declarar a data do fato, o status do próprio caso nem
     * escolher o protocolo. O andamento (status) muda depois pelo PUT, que é
     * a operação da fiscalização.
     */
    public Denuncia registrar(Denuncia denuncia) {
        if (texto(denuncia.getDescricao()) == null) {
            throw new IllegalArgumentException("Informe a descrição da denúncia.");
        }
        denuncia.setDescricao(denuncia.getDescricao().trim());

        resolverLote(denuncia);

        if (denuncia.getDescricao().length() > LIMITE_DESCRICAO) {
            throw new IllegalArgumentException(
                    "A descrição é longa demais (máximo de " + LIMITE_DESCRICAO + " caracteres).");
        }

        denuncia.setId(null);
        denuncia.setData(LocalDate.now().toString());
        denuncia.setStatus("ABERTA");
        denuncia.setProtocolo(gerarProtocolo());
        return denunciaRepository.save(denuncia);
    }

    /**
     * Garante que a denúncia só aponte para um lote que existe.
     *
     * Aceita o lote já vinculado (id) ou o código bruto lido pelo consumidor
     * (loteInformado). Se o código não consta na base — o caso de um QR
     * forjado —, a denúncia é registrada mesmo assim, com o código anotado na
     * descrição, em vez de perder a evidência ou falhar por chave estrangeira.
     */
    private void resolverLote(Denuncia denuncia) {
        String informado = texto(denuncia.getLoteInformado());
        Lote lote = denuncia.getLote();

        if (lote != null) {
            Integer id = lote.getId();
            Lote existente = id == null ? null : loteRepository.findById(id).orElse(null);
            if (existente == null && informado == null && id != null) {
                informado = String.valueOf(id);
            }
            lote = existente;
        }

        if (lote == null && informado != null) {
            if (informado.length() > LIMITE_CODIGO) {
                informado = informado.substring(0, LIMITE_CODIGO);
            }
            lote = loteRepository.buscarPorChave(informado).orElse(null);
            if (lote == null) {
                denuncia.setDescricao("[Código consultado sem registro na base: " + informado + "] "
                        + denuncia.getDescricao());
            }
        }
        denuncia.setLote(lote);
    }

    /** Devolve null quando o protocolo não existe. */
    public DenunciaPublica consultarPorProtocolo(String protocolo) {
        if (texto(protocolo) == null) {
            return null;
        }

        Denuncia denuncia = denunciaRepository
                .findByProtocolo(protocolo.trim().toUpperCase())
                .orElse(null);
        if (denuncia == null) {
            return null;
        }

        DenunciaPublica publica = new DenunciaPublica();
        publica.setProtocolo(denuncia.getProtocolo());
        publica.setData(denuncia.getData());
        publica.setStatus(denuncia.getStatus());
        publica.setDescricao(denuncia.getDescricao());
        publica.setLocalCompra(denuncia.getLocalCompra());
        publica.setLoteNumero(denuncia.getLote() != null
                ? denuncia.getLote().getNumeroLote() : null);

        Analise analise = analiseRepository.findByDenunciaId(denuncia.getId());
        if (analise != null) {
            publica.setResultadoAnalise(analise.getResultado());
            publica.setDataAnalise(analise.getData());
            publica.setLaboratorio(analise.getLaboratorio());
        }
        return publica;
    }

    /** Protocolo no formato SD-2026-7K3F9A, sem sequência previsível. */
    private String gerarProtocolo() {
        for (int tentativa = 0; tentativa < TENTATIVAS; tentativa++) {
            String candidato = "SD-" + LocalDate.now().getYear() + "-" + sorteia();
            if (denunciaRepository.findByProtocolo(candidato).isEmpty()) {
                return candidato;
            }
        }
        // Fallback improvável: acrescenta o instante em milissegundos
        return "SD-" + LocalDate.now().getYear() + "-" + System.currentTimeMillis();
    }

    private String sorteia() {
        StringBuilder texto = new StringBuilder(TAMANHO_PROTOCOLO);
        for (int i = 0; i < TAMANHO_PROTOCOLO; i++) {
            texto.append(ALFABETO.charAt(sorteio.nextInt(ALFABETO.length())));
        }
        return texto.toString();
    }

    private static String texto(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }
}
