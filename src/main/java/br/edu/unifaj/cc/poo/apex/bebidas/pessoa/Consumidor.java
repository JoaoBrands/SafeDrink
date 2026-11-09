package br.edu.unifaj.cc.poo.apex.bebidas.pessoa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "consumidor")
public class Consumidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String dataCadastro;

    private String endereco;
    private String cidade;
    private String estado;
}
