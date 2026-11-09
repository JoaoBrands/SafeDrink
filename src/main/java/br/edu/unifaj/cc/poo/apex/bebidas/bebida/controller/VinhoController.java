package br.edu.unifaj.cc.poo.apex.bebidas.bebida.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Vinho;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.VinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/vinho")
public class VinhoController {

    @Autowired
    VinhoRepository vinhoRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(vinhoRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Vinho vinho = vinhoRepository.findById(id).orElse(null);
            if (vinho == null) {
                return new Retorno("Vinho não encontrado: " + id);
            }
            return new Retorno(vinho);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Vinho vinho) {
        try {
            vinho.setId(null);
            return new Retorno(vinhoRepository.save(vinho));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Vinho vinho) {
        try {
            Vinho vinhoExistente = vinhoRepository.findById(id).orElse(null);
            if (vinhoExistente == null) {
                return new Retorno("Vinho não encontrado: " + id);
            }
            vinho.setId(id);
            return new Retorno(vinhoRepository.save(vinho));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            Vinho vinho = vinhoRepository.findById(id).orElse(null);
            if (vinho == null) {
                return new Retorno("Vinho não encontrado: " + id);
            }
            vinhoRepository.delete(vinho);
            return new Retorno("Vinho excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
