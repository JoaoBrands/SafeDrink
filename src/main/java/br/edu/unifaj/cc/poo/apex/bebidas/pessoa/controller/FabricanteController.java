package br.edu.unifaj.cc.poo.apex.bebidas.pessoa.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fabricante;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.FabricanteRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.seguranca.ControleAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Cadastro de fabricantes.
 *
 * É registro oficial (o campo "regular" entra no veredito de autenticidade),
 * então TODAS as operações exigem a chave da fiscalização.
 */
@RestController
@RequestMapping("/api/bebidas/fabricante")
public class FabricanteController {

    @Autowired
    FabricanteRepository fabricanteRepository;

    @Autowired
    ControleAcesso controleAcesso;

    @GetMapping
    public Retorno listar(@RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            return new Retorno(fabricanteRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id,
                         @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
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
    public Retorno incluir(@RequestBody Fabricante fabricante,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            fabricante.setId(null);
            return new Retorno(fabricanteRepository.save(fabricante));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Fabricante fabricante,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
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
    public Retorno excluir(@PathVariable Integer id,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
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
