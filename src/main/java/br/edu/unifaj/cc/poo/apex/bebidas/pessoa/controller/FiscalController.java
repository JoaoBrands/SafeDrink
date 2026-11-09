package br.edu.unifaj.cc.poo.apex.bebidas.pessoa.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fiscal;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.FiscalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/fiscal")
public class FiscalController {

    @Autowired
    FiscalRepository fiscalRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(fiscalRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Fiscal fiscal = fiscalRepository.findById(id).orElse(null);
            if (fiscal == null) {
                return new Retorno("Fiscal não encontrado: " + id);
            }
            return new Retorno(fiscal);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Fiscal fiscal) {
        try {
            fiscal.setId(null);
            return new Retorno(fiscalRepository.save(fiscal));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Fiscal fiscal) {
        try {
            Fiscal fiscalExistente = fiscalRepository.findById(id).orElse(null);
            if (fiscalExistente == null) {
                return new Retorno("Fiscal não encontrado: " + id);
            }
            fiscal.setId(id);
            return new Retorno(fiscalRepository.save(fiscal));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            Fiscal fiscal = fiscalRepository.findById(id).orElse(null);
            if (fiscal == null) {
                return new Retorno("Fiscal não encontrado: " + id);
            }
            fiscalRepository.delete(fiscal);
            return new Retorno("Fiscal excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
