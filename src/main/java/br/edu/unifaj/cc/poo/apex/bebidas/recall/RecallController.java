package br.edu.unifaj.cc.poo.apex.bebidas.recall;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.seguranca.ControleAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Recalls (recolhimento de lotes). Abrir ou encerrar um recall muda o veredito
 * de autenticidade e dispara aviso ao consumidor, então todas as operações
 * exigem a chave da fiscalização.
 *
 * A leitura pública dos avisos é feita por /api/bebidas/aviso, que é aberta.
 */
@RestController
@RequestMapping("/api/bebidas/recall")
public class RecallController {

    @Autowired
    RecallRepository recallRepository;

    @Autowired
    ControleAcesso controleAcesso;

    @GetMapping
    public Retorno listar(@RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            return new Retorno(recallRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id,
                         @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Recall recall = recallRepository.findById(id).orElse(null);
            if (recall == null) {
                return new Retorno("Recall não encontrado: " + id);
            }
            return new Retorno(recall);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Recall recall,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            recall.setId(null);
            return new Retorno(recallRepository.save(recall));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Recall recall,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Recall recallExistente = recallRepository.findById(id).orElse(null);
            if (recallExistente == null) {
                return new Retorno("Recall não encontrado: " + id);
            }
            recall.setId(id);
            return new Retorno(recallRepository.save(recall));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Recall recall = recallRepository.findById(id).orElse(null);
            if (recall == null) {
                return new Retorno("Recall não encontrado: " + id);
            }
            recallRepository.delete(recall);
            return new Retorno("Recall excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
