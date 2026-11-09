package br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.PontoVenda;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.repository.PontoVendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/pontovenda")
public class PontoVendaController {

    @Autowired
    PontoVendaRepository pontoVendaRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(pontoVendaRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            PontoVenda pontoVenda = pontoVendaRepository.findById(id).orElse(null);
            if (pontoVenda == null) {
                return new Retorno("Ponto de Venda não encontrado: " + id);
            }
            return new Retorno(pontoVenda);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody PontoVenda pontoVenda) {
        try {
            pontoVenda.setId(null);
            return new Retorno(pontoVendaRepository.save(pontoVenda));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody PontoVenda pontoVenda) {
        try {
            PontoVenda pontoVendaExistente = pontoVendaRepository.findById(id).orElse(null);
            if (pontoVendaExistente == null) {
                return new Retorno("Ponto de Venda não encontrado: " + id);
            }
            pontoVenda.setId(id);
            return new Retorno(pontoVendaRepository.save(pontoVenda));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id) {
        try {
            PontoVenda pontoVenda = pontoVendaRepository.findById(id).orElse(null);
            if (pontoVenda == null) {
                return new Retorno("Ponto de Venda não encontrado: " + id);
            }
            pontoVendaRepository.delete(pontoVenda);
            return new Retorno("Ponto de Venda excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}

