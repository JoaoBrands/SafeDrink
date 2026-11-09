package br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.Distribuidor;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.repository.DistribuidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/distribuidor")
public class DistribuidorController {

    @Autowired
    DistribuidorRepository distribuidorRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(distribuidorRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Distribuidor distribuidor = distribuidorRepository.findById(id).orElse(null);
            if (distribuidor == null) {
                return new Retorno("Distribuidor não encontrado: " + id);
            }
            return new Retorno(distribuidor);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Distribuidor distribuidor) {
        try {
            distribuidor.setId(null);
            return new Retorno(distribuidorRepository.save(distribuidor));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Distribuidor distribuidor) {
        try {
            Distribuidor distribuidorExistente = distribuidorRepository.findById(id).orElse(null);
            if (distribuidorExistente == null) {
                return new Retorno("Distribuidor não encontrado: " + id);
            }
            distribuidor.setId(id);
            return new Retorno(distribuidorRepository.save(distribuidor));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            Distribuidor distribuidor = distribuidorRepository.findById(id).orElse(null);
            if (distribuidor == null) {
                return new Retorno("Distribuidor não encontrado: " + id);
            }
            distribuidorRepository.delete(distribuidor);
            return new Retorno("Distribuidor excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
