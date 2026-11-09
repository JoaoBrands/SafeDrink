package br.edu.unifaj.cc.poo.apex.bebidas.pessoa.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fabricante;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.FabricanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/fabricante")
public class FabricanteController {

    @Autowired
    FabricanteRepository fabricanteRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(fabricanteRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Fabricante fabricante = fabricanteRepository.findById(id).orElse(null);
            if (fabricante == null) {
                return new Retorno("Fabricante não encontrado: " + id);
            }
            return new Retorno(fabricante);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Fabricante fabricante) {
        try {
            fabricante.setId(null);
            return new Retorno(fabricanteRepository.save(fabricante));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Fabricante fabricante) {
        try {
            Fabricante fabricanteExistente = fabricanteRepository.findById(id).orElse(null);
            if (fabricanteExistente == null) {
                return new Retorno("Fabricante não encontrado: " + id);
            }
            fabricante.setId(id);
            return new Retorno(fabricanteRepository.save(fabricante));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            Fabricante fabricante = fabricanteRepository.findById(id).orElse(null);
            if (fabricante == null) {
                return new Retorno("Fabricante não encontrado: " + id);
            }
            fabricanteRepository.delete(fabricante);
            return new Retorno("Fabricante excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
