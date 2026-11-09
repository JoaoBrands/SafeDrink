package br.edu.unifaj.cc.poo.apex.bebidas.recall;

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
@Table(name = "recall")
public class Recall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String dataInicio;
    private String dataFim;
    private String motivo;
    private String status;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;

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
