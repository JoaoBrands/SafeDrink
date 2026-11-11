package br.edu.unifaj.cc.poo.apex.bebidas.denuncia;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import br.edu.unifaj.cc.poo.apex.bebidas.seguranca.ControleAcesso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Denúncias.
 *
 * O canal do consumidor é aberto: POST (registrar) e GET /protocolo/{p}
 * (acompanhar). A LISTAGEM e a leitura por id expõem dados pessoais de quem
 * denunciou, então exigem a chave da fiscalização.
 */
@RestController
@RequestMapping("/api/bebidas/denuncia")
public class DenunciaController {

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    DenunciaService denunciaService;

    @Autowired
    ControleAcesso controleAcesso;

    @GetMapping
    public Retorno listar(@RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            return new Retorno(denunciaRepository.findAll());
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /**
     * Consulta pública: o consumidor acompanha a denúncia pelo protocolo.
     * Devolve apenas dados não pessoais (ver DenunciaPublica).
     */
    @GetMapping("/protocolo/{protocolo}")
    public Retorno obterPorProtocolo(@PathVariable String protocolo) {
        try {
            DenunciaPublica publica = denunciaService.consultarPorProtocolo(protocolo);
            if (publica == null) {
                return new Retorno("Protocolo não encontrado: " + protocolo);
            }
            return new Retorno(publica);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Retorno obter(@PathVariable Integer id,
                         @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Denuncia denuncia = denunciaRepository.findById(id).orElse(null);
            if (denuncia == null) {
                return new Retorno("Denúncia não encontrada: " + id);
            }
            return new Retorno(denuncia);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    /** Público: é o canal do consumidor. */
    @PostMapping
    public Retorno incluir(@RequestBody Denuncia denuncia) {
        try {
            // Data, status inicial e protocolo são definidos pelo servidor.
            return new Retorno(denunciaService.registrar(denuncia));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Retorno alterar(@PathVariable Integer id, @RequestBody Denuncia denuncia,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Denuncia denunciaExistente = denunciaRepository.findById(id).orElse(null);
            if (denunciaExistente == null) {
                return new Retorno("Denúncia não encontrada: " + id);
            }
            denuncia.setId(id);
            // O protocolo é do servidor: o painel não o envia e não pode apagá-lo.
            denuncia.setProtocolo(denunciaExistente.getProtocolo());
            if (denuncia.getData() == null || denuncia.getData().isBlank()) {
                denuncia.setData(denunciaExistente.getData());
            }
            return new Retorno(denunciaRepository.save(denuncia));
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Retorno excluir(@PathVariable Integer id,
                           @RequestHeader(value = "X-Chave-Escrita", required = false) String chave) {
        try {
            controleAcesso.exigirChave(chave);
            Denuncia denuncia = denunciaRepository.findById(id).orElse(null);
            if (denuncia == null) {
                return new Retorno("Denúncia não encontrada: " + id);
            }
            denunciaRepository.delete(denuncia);
            return new Retorno("Denúncia excluída com sucesso: " + id);
        } catch (Exception ex) {
            return new Retorno(ex.getMessage());
        }
    }
}
