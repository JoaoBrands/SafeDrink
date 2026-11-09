package br.edu.unifaj.cc.poo.apex.bebidas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RastreioBebidasApplication {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  RASTREIO DE BEBIDAS FALSIFICADAS");
        System.out.println("  Joao Gabriel Brandao - RA: 12529615");
        System.out.println("  Joao Gabriel Oliveira - RA: 12529796 ");
        System.out.println("==========================================");
        SpringApplication.run(RastreioBebidasApplication.class, args);
    }
}
