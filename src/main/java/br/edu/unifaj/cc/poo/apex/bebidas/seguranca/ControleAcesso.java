package br.edu.unifaj.cc.poo.apex.bebidas.seguranca;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Controle de acesso às operações da fiscalização.
 *
 * A chave fica em application.properties (safedrink.chave-escrita) e chega
 * pelo cabeçalho HTTP "X-Chave-Escrita". A classe é um singleton gerenciado
 * pelo Spring, como os repositórios.
 *
 * LIMITE CONHECIDO: é uma chave compartilhada, adequada para demonstração.
 * Não substitui autenticação com usuário e perfil, que está fora do escopo
 * da disciplina.
 */
@Component
public class ControleAcesso {

    private final String chaveEsperada;

    public ControleAcesso(@Value("${safedrink.chave-escrita}") String chaveEsperada) {
        this.chaveEsperada = chaveEsperada;
    }

    /**
     * Lança AcessoNegadoException quando a chave não confere.
     * O controller que chamou não precisa tratar: o catch (Exception) já
     * devolve um Retorno com a mensagem amigável.
     */
    public void exigirChave(String chaveEnviada) {
        if (chaveEnviada == null || !chaveEsperada.equals(chaveEnviada.trim())) {
            throw new AcessoNegadoException(
                    "Chave de escrita ausente ou inválida. Informe o cabeçalho X-Chave-Escrita.");
        }
    }
}
