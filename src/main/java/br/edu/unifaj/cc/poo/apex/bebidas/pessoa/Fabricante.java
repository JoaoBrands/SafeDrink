package br.edu.unifaj.cc.poo.apex.bebidas.pessoa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fabricante")
public class Fabricante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String dataCadastro;

    private String cnpj;
    private String razaoSocial;

    @Column(name = "registro_mapa")
    private String registroMAPA;

    private String paisOrigem;
    private Boolean regular;
}
