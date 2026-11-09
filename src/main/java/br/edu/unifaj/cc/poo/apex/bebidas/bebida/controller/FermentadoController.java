package br.edu.unifaj.cc.poo.apex.bebidas.bebida.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Fermentado;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.FermentadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/fermentado")
public class FermentadoController {

    @Autowired
    FermentadoRepository fermentadoRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(fermentadoRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Fermentado fermentado = fermentadoRepository.findById(id).orElse(null);
            if (fermentado == null) {
                return new Retorno("Fermentado não encontrado: " + id);
            }
            return new Retorno(fermentado);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Fermentado fermentado) {
        try {
            fermentado.setId(null);
            return new Retorno(fermentadoRepository.save(fermentado));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Fermentado fermentado) {
        try {
            Fermentado fermentadoExistente = fermentadoRepository.findById(id).orElse(null);
            if (fermentadoExistente == null) {
                return new Retorno("Fermentado não encontrado: " + id);
            }
            fermentado.setId(id);
            return new Retorno(fermentadoRepository.save(fermentado));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            Fermentado fermentado = fermentadoRepository.findById(id).orElse(null);
            if (fermentado == null) {
                return new Retorno("Fermentado não encontrado: " + id);
            }
            fermentadoRepository.delete(fermentado);
            return new Retorno("Fermentado excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}