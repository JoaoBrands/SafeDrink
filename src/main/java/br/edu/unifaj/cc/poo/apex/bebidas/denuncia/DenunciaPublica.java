package br.edu.unifaj.cc.poo.apex.bebidas.denuncia;

import lombok.Data;

/**
 * Visão PÚBLICA de uma denúncia, para o consumidor acompanhar pelo protocolo.
 *
 * É proposital que aqui não exista Consumidor, cpf, e-mail, telefone, contato
 * nem o id interno: o protocolo é a única chave, e o retorno não vaza dados
 * pessoais de quem denunciou.
 */
@Data
public class DenunciaPublica {

    private String protocolo;
    private String data;
    private String status;
    private String descricao;
    private String localCompra;

    /** Número do lote reclamado (não o objeto inteiro). */
    private String loteNumero;

    // Resultado da fiscalização, quando já existe laudo
    private String resultadoAnalise;
    private String dataAnalise;
    private String laboratorio;
}
