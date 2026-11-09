package br.edu.unifaj.cc.poo.apex.bebidas.denuncia;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/denuncia")
public class DenunciaController {

    @Autowired
    DenunciaRepository denunciaRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(denunciaRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Denuncia denuncia = denunciaRepository.findById(id).orElse(null);
            if (denuncia == null) {
                return new Retorno("Denúncia não encontrada: " + id);
            }
            return new Retorno(denuncia);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Denuncia denuncia) {
        try {
            denuncia.setId(null);
            return new Retorno(denunciaRepository.save(denuncia));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Denuncia denuncia) {
        try {
            Denuncia denunciaExistente = denunciaRepository.findById(id).orElse(null);
            if (denunciaExistente == null) {
                return new Retorno("Denúncia não encontrada: " + id);
            }
            denuncia.setId(id);
            return new Retorno(denunciaRepository.save(denuncia));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            Denuncia denuncia = denunciaRepository.findById(id).orElse(null);
            if (denuncia == null) {
                return new Retorno("Denúncia não encontrada: " + id);
            }
            denunciaRepository.delete(denuncia);
            return new Retorno("Denúncia excluída com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}