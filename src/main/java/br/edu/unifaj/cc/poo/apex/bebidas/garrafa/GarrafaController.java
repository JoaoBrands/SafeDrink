package br.edu.unifaj.cc.poo.apex.bebidas.garrafa;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/garrafa")
public class GarrafaController {

    @Autowired
    GarrafaRepository garrafaRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(garrafaRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Garrafa garrafa = garrafaRepository.findById(id).orElse(null);
            if (garrafa == null) {
                return new Retorno("Garrafa não encontrada: " + id);
            }
            return new Retorno(garrafa);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/codigo/{codigoBarras}")
    public Retorno obterPorCodigoBarras(@PathVariable String codigoBarras) {
        try {
            Garrafa garrafa = garrafaRepository.findByCodigoBarras(codigoBarras);
            if (garrafa == null) {
                return new Retorno("Garrafa não encontrada para o código: " + codigoBarras);
            }
            return new Retorno(garrafa);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Garrafa garrafa) {
        try {
            garrafa.setId(null);
            return new Retorno(garrafaRepository.save(garrafa));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Garrafa garrafa) {
        try {
            Garrafa garrafaExistente = garrafaRepository.findById(id).orElse(null);
            if (garrafaExistente == null) {
                return new Retorno("Garrafa não encontrada: " + id);
            }
            garrafa.setId(id);
            return new Retorno(garrafaRepository.save(garrafa));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            Garrafa garrafa = garrafaRepository.findById(id).orElse(null);
            if (garrafa == null) {
                return new Retorno("Garrafa não encontrada: " + id);
            }
            garrafaRepository.delete(garrafa);
            return new Retorno("Garrafa excluída com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}