package br.edu.unifaj.cc.poo.apex.bebidas.analise;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.seguranca.ControleAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Laudos periciais. O resultado da análise entra no veredito de autenticidade,
 * então todas as operações exigem a chave da fiscalização.
 */
@RestController
@RequestMapping("/api/bebidas/analise")
public class AnaliseController {

    @Autowired
    AnaliseRepository analiseRepository;

    @Autowired
    ControleAcesso controleAcesso;

    @GetMapping
    public Retorno listar(@RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            return new Retorno(analiseRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id,
                         @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Analise analise = analiseRepository.findById(id).orElse(null);
            if (analise == null) {
                return new Retorno("Análise não encontrada: " + id);
            }
            return new Retorno(analise);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Analise analise,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            analise.setId(null);
            return new Retorno(analiseRepository.save(analise));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Analise analise,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Analise analiseExistente = analiseRepository.findById(id).orElse(null);
            if (analiseExistente == null) {
                return new Retorno("Análise não encontrada: " + id);
            }
            analise.setId(id);
            return new Retorno(analiseRepository.save(analise));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Analise analise = analiseRepository.findById(id).orElse(null);
            if (analise == null) {
                return new Retorno("Análise não encontrada: " + id);
            }
            analiseRepository.delete(analise);
            return new Retorno("Análise excluída com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
