package br.edu.unifaj.cc.poo.apex.bebidas.distribuidor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "distribuidor")
public class Distribuidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cnpj;
    private String razaoSocial;
    private String regiao;
    private String contato;

    @JsonIgnore
    @OneToMany(mappedBy = "distribuidor")
    private List<PontoVenda> pontosVenda = new ArrayList<>();
}
