package br.edu.unifaj.cc.poo.apex.bebidas.analise;

import br.edu.unifaj.cc.poo.apex.bebidas.denuncia.Denuncia;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fiscal;
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
@Table(name = "analise")
public class Analise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String data;
    private String resultado;
    private String laudo;
    private String laboratorio;

    @ManyToOne
    @JoinColumn(name = "fiscal_id")
    private Fiscal fiscal;

    @ManyToOne
    @JoinColumn(name = "denuncia_id")
    private Denuncia denuncia;

    public Integer getFiscalId() {
        return fiscal != null ? fiscal.getId() : null;
    }

    public void setFiscalId(Integer fiscalId) {
        if (fiscalId != null) {
            if (this.fiscal == null) {
                this.fiscal = new Fiscal();
            }
            this.fiscal.setId(fiscalId);
        }
    }

    public Integer getDenunciaId() {
        return denuncia != null ? denuncia.getId() : null;
    }

    public void setDenunciaId(Integer denunciaId) {
        if (denunciaId != null) {
            if (this.denuncia == null) {
                this.denuncia = new Denuncia();
            }
            this.denuncia.setId(denunciaId);
        }
    }
}
