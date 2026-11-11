package br.edu.unifaj.cc.poo.apex.bebidas.garrafa;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.seguranca.ControleAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Garrafas (selo por unidade).
 *
 * O status da garrafa ("FALSIFICADA") entra no veredito, então toda operação
 * exige a chave da fiscalização. O consumidor consulta pelo endpoint público
 * /api/bebidas/validacao.
 */
@RestController
@RequestMapping("/api/bebidas/garrafa")
public class GarrafaController {

    @Autowired
    GarrafaRepository garrafaRepository;

    @Autowired
    ControleAcesso controleAcesso;

    @GetMapping
    public Retorno listar(@RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            return new Retorno(garrafaRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id,
                         @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
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
    public Retorno obterPorCodigoBarras(@PathVariable String codigoBarras,
                                        @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
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
    public Retorno incluir(@RequestBody Garrafa garrafa,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            garrafa.setId(null);
            return new Retorno(garrafaRepository.save(garrafa));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Garrafa garrafa,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
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
    public Retorno excluir(@PathVariable Integer id,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
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
