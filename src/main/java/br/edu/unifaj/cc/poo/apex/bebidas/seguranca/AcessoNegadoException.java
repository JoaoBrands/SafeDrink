package br.edu.unifaj.cc.poo.apex.bebidas.seguranca;

/**
 * Exceção de acesso negado.
 *
 * É RuntimeException para não obrigar "throws" em todos os controllers: os
 * métodos já têm "catch (Exception ex)", que transforma a mensagem no objeto
 * Retorno devolvido ao cliente.
 */
public class AcessoNegadoException extends RuntimeException {

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
