package br.edu.unifaj.cc.poo.apex.bebidas.pessoa.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Consumidor;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.ConsumidorRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/consumidor")
public class ConsumidorController {

    @Autowired
    ConsumidorRepository consumidorRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(consumidorRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Consumidor consumidor = consumidorRepository.findById(id).orElse(null);
            if (consumidor == null) {
                return new Retorno("Consumidor não encontrado: " + id);
            }
            return new Retorno(consumidor);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Consumidor consumidor) {
        try {
            consumidor.setId(null);
            return new Retorno(consumidorRepository.save(consumidor));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Consumidor consumidor) {
        try {
            Consumidor consumidorExistente = consumidorRepository.findById(id).orElse(null);
            if (consumidorExistente == null) {
                return new Retorno("Consumidor não encontrado: " + id);
            }
            consumidor.setId(id);
            return new Retorno(consumidorRepository.save(consumidor));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            Consumidor consumidor = consumidorRepository.findById(id).orElse(null);
            if (consumidor == null) {
                return new Retorno("Consumidor não encontrado: " + id);
            }
            consumidorRepository.delete(consumidor);
            return new Retorno("Consumidor excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
