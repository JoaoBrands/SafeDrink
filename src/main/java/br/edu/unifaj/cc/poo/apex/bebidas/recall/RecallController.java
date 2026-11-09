package br.edu.unifaj.cc.poo.apex.bebidas.recall;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/recall")
public class RecallController {

    @Autowired
    RecallRepository recallRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(recallRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
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
    public Retorno incluir(@RequestBody Recall recall) {
        try {
            recall.setId(null);
            return new Retorno(recallRepository.save(recall));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Recall recall) {
        try {
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
    public Retorno excluir(@PathVariable Integer id) {
        try {
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