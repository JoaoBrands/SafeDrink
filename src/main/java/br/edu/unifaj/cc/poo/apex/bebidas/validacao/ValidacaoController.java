package br.edu.unifaj.cc.poo.apex.bebidas.validacao;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Consulta pública de autenticidade.
 *
 * É o endpoint que o consumidor usa de fato (QR Code, número do lote ou código
 * de barras). Devolve o veredito com os motivos, em vez de um simples sim/não.
 */
@RestController
@RequestMapping("/api/bebidas/validacao")
public class ValidacaoController {

    private final MotorValidacao motorValidacao;

    public ValidacaoController(MotorValidacao motorValidacao) {
        this.motorValidacao = motorValidacao;
    }

    /** Aceita o ID do lote ou o número impresso no rótulo (ex: LOT-2026-FAL02). */
    @GetMapping("/lote/{chave}")
    public Retorno validarLote(@PathVariable String chave) {
        try {
            return new Retorno(motorValidacao.validarPorChave(chave));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /** Consulta pelo código de barras (EAN) da garrafa. */
    @GetMapping("/codigo-barras/{codigoBarras}")
    public Retorno validarCodigoBarras(@PathVariable String codigoBarras) {
        try {
            return new Retorno(motorValidacao.validarPorCodigoBarras(codigoBarras));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
