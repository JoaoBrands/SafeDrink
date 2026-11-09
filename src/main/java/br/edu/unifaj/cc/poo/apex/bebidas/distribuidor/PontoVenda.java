package br.edu.unifaj.cc.poo.apex.bebidas.distribuidor;

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
@Table(name = "ponto_venda")
public class PontoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cnpj;
    private String nome;
    private String endereco;
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "distribuidor_id")
    private Distribuidor distribuidor;

    public Integer getDistribuidorId() {
        return distribuidor != null ? distribuidor.getId() : null;
    }

    public void setDistribuidorId(Integer distribuidorId) {
        if (distribuidorId != null) {
            if (this.distribuidor == null) {
                this.distribuidor = new Distribuidor();
            }
            this.distribuidor.setId(distribuidorId);
        }
    }
}
