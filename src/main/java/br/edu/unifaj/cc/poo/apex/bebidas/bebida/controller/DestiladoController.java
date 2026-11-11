package br.edu.unifaj.cc.poo.apex.bebidas.bebida.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Destilado;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.DestiladoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import br.edu.unifaj.cc.poo.apex.bebidas.seguranca.ControleAcesso;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/destilado")
public class DestiladoController {

    @Autowired
    ControleAcesso controleAcesso;

    @Autowired
    DestiladoRepository destiladoRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(destiladoRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Destilado destilado = destiladoRepository.findById(id).orElse(null);
            if (destilado == null) {
                return new Retorno("Destilado não encontrado: " + id);
            }
            return new Retorno(destilado);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Destilado destilado, @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            destilado.setId(null);
            return new Retorno(destiladoRepository.save(destilado));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Destilado destilado, @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Destilado destiladoExistente = destiladoRepository.findById(id).orElse(null);
            if (destiladoExistente == null) {
                return new Retorno("Destilado não encontrado: " + id);
            }
            destilado.setId(id);
            return new Retorno(destiladoRepository.save(destilado));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id, @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Destilado destilado = destiladoRepository.findById(id).orElse(null);
            if (destilado == null) {
                return new Retorno("Destilado não encontrado: " + id);
            }
            destiladoRepository.delete(destilado);
            return new Retorno("Destilado excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
