package br.edu.unifaj.cc.poo.apex.bebidas.bebida.controller;

import br.edu.unifaj.cc.poo.apex.bebidas.Retorno;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/bebidas/external")
public class BebidaExternalController {

    @GetMapping("/openfoodfacts/{ean}")
    public Retorno consultarOpenFoodFacts(@PathVariable String ean) {
        try {
            String url = "https://br.openfoodfacts.org/api/v2/product/" + ean + ".json";
            RestTemplate restTemplate = new RestTemplate();
            Object resposta = restTemplate.getForObject(url, Object.class);
            return new Retorno(resposta);
        } catch (Exception ex) {
            return new Retorno("Erro ao consultar Open Food Facts: " + ex.getMessage());
        }
    }
}