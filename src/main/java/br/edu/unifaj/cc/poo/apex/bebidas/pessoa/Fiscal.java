package br.edu.unifaj.cc.poo.apex.bebidas.pessoa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "fiscal")
public class Fiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String dataCadastro;

    private String matricula;
    private String orgao;
    private String regiaoAtuacao;
}
