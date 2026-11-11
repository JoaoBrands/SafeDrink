package br.edu.unifaj.cc.poo.apex.bebidas.aviso;

import lombok.Data;

/**
 * Item do mural público de avisos ao consumidor.
 *
 * Junta recall (recolhimento) e alerta (comunicado) em um formato plano,
 * pronto para uma lista no aplicativo — sem os grafos aninhados do CRUD.
 */
@Data
public class Aviso {

    /** RECALL (recolhimento de lote) ou ALERTA (comunicado sanitário). */
    private String tipo;

    private String nivel;
    private String data;
    private String regiao;
    private String titulo;
    private String descricao;
    private String loteNumero;
    private String fabricante;
    private String status;
}
