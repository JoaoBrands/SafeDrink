package br.edu.unifaj.cc.poo.apex.bebidas.alerta;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.beans.factory.annotation.Autowired;
import br.edu.unifaj.cc.poo.apex.bebidas.seguranca.ControleAcesso;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bebidas/alerta")
public class AlertaController {

    @Autowired
    ControleAcesso controleAcesso;

    @Autowired
    AlertaRepository alertaRepository;

    @GetMapping
    public Retorno listar() {
        try {
            return new Retorno(alertaRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id) {
        try {
            Alerta alerta = alertaRepository.findById(id).orElse(null);
            if (alerta == null) {
                return new Retorno("Alerta não encontrado: " + id);
            }
            return new Retorno(alerta);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PostMapping
    public Retorno incluir(@RequestBody Alerta alerta, @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            alerta.setId(null);
            return new Retorno(alertaRepository.save(alerta));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Alerta alerta, @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Alerta alertaExistente = alertaRepository.findById(id).orElse(null);
            if (alertaExistente == null) {
                return new Retorno("Alerta não encontrado: " + id);
            }
            alerta.setId(id);
            return new Retorno(alertaRepository.save(alerta));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id, @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Alerta alerta = alertaRepository.findById(id).orElse(null);
            if (alerta == null) {
                return new Retorno("Alerta não encontrado: " + id);
            }
            alertaRepository.delete(alerta);
            return new Retorno("Alerta excluído com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}