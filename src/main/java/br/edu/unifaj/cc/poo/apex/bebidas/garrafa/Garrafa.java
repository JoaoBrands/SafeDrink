package br.edu.unifaj.cc.poo.apex.bebidas.garrafa;

import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.PontoVenda;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.Lote;
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
@Table(name = "garrafa")
public class Garrafa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codigoBarras;
    private String dataEnvase;
    private String seloFiscal;
    private String status;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @ManyToOne
    @JoinColumn(name = "ponto_venda_id")
    private PontoVenda pontoVenda;

    public Integer getLoteId() {
        return lote != null ? lote.getId() : null;
    }

    public void setLoteId(Integer loteId) {
        if (loteId != null) {
            if (this.lote == null) {
                this.lote = new Lote();
            }
            this.lote.setId(loteId);
        }
    }
}
