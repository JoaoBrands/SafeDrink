package br.edu.unifaj.cc.poo.apex.bebidas.bebida.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Consulta externa de produto por código de barras (EAN).
 *
 * Fonte: API de Produtos (api-produtos.seunegocionanuvem.com.br), que devolve
 *   GET /api/{ean}              -> imagem do produto em base64
 *   GET /api/tributacao/{ean}   -> dados fiscais (descrição, NCM, CEST, ANVISA)
 *
 * A chamada é feita no SERVIDOR: a página nunca fala direto com o terceiro,
 * então o endereço externo fica em um lugar só.
 */
@RestController
@RequestMapping("/api/bebidas/external")
public class BebidaExternalController {

    private static final String BASE = "https://api-produtos.seunegocionanuvem.com.br/api";

    private final RestTemplate restTemplate = new RestTemplate();

    /** Dados fiscais do produto: descricao, ncm, cest, medida, cod_anvisa… */
    @GetMapping("/tributacao/{ean}")
    public Retorno consultarTributacao(@PathVariable String ean) {
        try {
            Map<?, ?> resposta = restTemplate.getForObject(
                    BASE + "/tributacao/" + ean, Map.class);
            Object dados = (resposta == null) ? null : resposta.get("data");
            if (dados == null) {
                return new Retorno("Produto não encontrado para o código " + ean + ".");
            }
            return new Retorno(dados);
        } catch (HttpClientErrorException naoEncontrado) {
            return new Retorno("Produto não encontrado para o código " + ean + ".");
        } catch (Exception ex) {
            return new Retorno("Erro ao consultar a API de produtos: " + ex.getMessage());
        }
    }

    /** Imagem do produto: devolve imagem_base64 e mime_type. */
    @GetMapping("/imagem/{ean}")
    public Retorno consultarImagem(@PathVariable String ean) {
        try {
            Map<?, ?> resposta = restTemplate.getForObject(BASE + "/" + ean, Map.class);
            if (resposta == null || resposta.get("imagem_base64") == null) {
                return new Retorno("Imagem não encontrada para o código " + ean + ".");
            }
            return new Retorno(resposta);
        } catch (HttpClientErrorException naoEncontrado) {
            return new Retorno("Imagem não encontrada para o código " + ean + ".");
        } catch (Exception ex) {
            return new Retorno("Erro ao consultar a API de produtos: " + ex.getMessage());
        }
    }
}
