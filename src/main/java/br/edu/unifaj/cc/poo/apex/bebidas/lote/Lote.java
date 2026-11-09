package br.edu.unifaj.cc.poo.apex.bebidas.lote;

import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Destilado;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Fermentado;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Vinho;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.Distribuidor;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fabricante;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "lote")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String numeroLote;
    private String dataFabricacao;
    private String dataValidade;
    private Integer quantidade;
    private Boolean suspeito;

    @ManyToOne
    @JoinColumn(name = "fabricante_id")
    private Fabricante fabricante;

    @ManyToOne
    @JoinColumn(name = "distribuidor_id")
    private Distribuidor distribuidor;

    @ManyToOne
    @JoinColumn(name = "destilado_id")
    private Destilado destilado;

    @ManyToOne
    @JoinColumn(name = "fermentado_id")
    private Fermentado fermentado;

    @ManyToOne
    @JoinColumn(name = "vinho_id")
    private Vinho vinho;
}