package br.edu.unifaj.cc.poo.apex.bebidas.lote;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.seguranca.ControleAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Lotes.
 *
 * A LEITURA de um lote continua pública (GET /{id}) porque o aplicativo do
 * consumidor precisa dela ao registrar uma denúncia. Escrita e listagem
 * completa exigem a chave da fiscalização.
 */
@RestController
@RequestMapping("/api/bebidas/lote")
public class LoteController {

    @Autowired
    LoteRepository loteRepository;

    @Autowired
    ControleAcesso controleAcesso;

    @GetMapping
    public Retorno listar(@RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            return new Retorno(loteRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /** Público: usado pelo app do consumidor ao denunciar um lote. */
    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Lote lote = loteRepository.findById(id).orElse(null);
            if (lote == null) {
                return new Retorno("Lote não encontrado: " + id);
            }
            return new Retorno(lote);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Lote lote,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            lote.setId(null);
            return new Retorno(loteRepository.save(lote));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Lote lote,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Lote loteExistente = loteRepository.findById(id).orElse(null);
            if (loteExistente == null) {
                return new Retorno("Lote não encontrado: " + id);
            }
            lote.setId(id);
            return new Retorno(loteRepository.save(lote));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Lote lote = loteRepository.findById(id).orElse(null);
            if (lote == null) {
                return new Retorno("Lote não encontrado: " + id);
            }
            loteRepository.delete(lote);
            return new Retorno("Lote excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
