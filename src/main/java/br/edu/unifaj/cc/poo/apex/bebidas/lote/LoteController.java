package br.edu.unifaj.cc.poo.apex.bebidas.lote;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/lote")
public class LoteController {

    @Autowired
    LoteRepository loteRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(loteRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

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
    public Retorno incluir(@RequestBody Lote lote) {
        try {
            lote.setId(null);
            return new Retorno(loteRepository.save(lote));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Lote lote) {
        try {
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
    public Retorno excluir(@PathVariable Integer id) {
        try {
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