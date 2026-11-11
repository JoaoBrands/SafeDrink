package br.edu.unifaj.cc.poo.apex.bebidas.validacao;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Veredito da consulta de autenticidade de um lote ou de uma garrafa.
 *
 * Diferente do resto da API, aqui "não encontrado" NÃO é erro: é um veredito
 * válido (NAO_ENCONTRADO), porque para o consumidor um registro inexistente é
 * justamente o sinal de alerta mais importante.
 */
@Data
public class ResultadoValidacao {

    /** Lote/garrafa conferido: nada indica problema. */
    public static final String AUTENTICO = "AUTENTICO";

    /** O registro existe, mas há pendência (validade vencida, sem selo, etc.). */
    public static final String ATENCAO = "ATENCAO";

    /** Há evidência objetiva de fraude: não consumir. */
    public static final String SUSPEITO = "SUSPEITO";

    /** O código consultado não consta na base oficial. */
    public static final String NAO_ENCONTRADO = "NAO_ENCONTRADO";

    private String nivel;
    private String titulo;
    private String mensagem;

    /** Explicações objetivas que levaram ao veredito (auditáveis). */
    private List<String> motivos = new ArrayList<>();

    // ---- Lote ----
    private Integer loteId;
    private String numeroLote;
    private String bebida;
    private String fabricante;
    private Boolean fabricanteRegular;
    private String registroMAPA;
    private String distribuidor;
    private String dataFabricacao;
    private String dataValidade;
    private Integer quantidade;
    private Boolean vencido;
    private Long diasParaVencer;
    private Integer garrafasFalsificadas;
    private Integer totalGarrafas;
    private Boolean temRecall;

    // ---- Garrafa (quando a consulta é pelo selo/código) ----
    private String codigoBarras;
    private String seloFiscal;
    private String statusGarrafa;
    private String pontoVenda;
}
