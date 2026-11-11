package br.edu.unifaj.cc.poo.apex.bebidas.denuncia;

import br.edu.unifaj.cc.poo.apex.bebidas.garrafa.Garrafa;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.Lote;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Consumidor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "denuncia")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Protocolo de acompanhamento devolvido ao consumidor (ex.: SD-2026-7K3F9A).
     * Não é sequencial: sem ele ninguém consulta a denúncia de outra pessoa.
     */
    private String protocolo;

    private String data;
    private String descricao;
    private String status;

    @ManyToOne
    @JoinColumn(name = "consumidor_id")
    private Consumidor consumidor;

    @ManyToOne
    @JoinColumn(name = "garrafa_id")
    private Garrafa garrafa;

    // Denúncia feita pelo consumidor a partir do QR Code (que identifica o lote)
    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;

    private String localCompra;
    private String contato;

    /**
     * Código do lote como o consumidor o leu (ID ou número do rótulo). Não é
     * gravado: o DenunciaService usa para achar o lote ou, se ele não existir,
     * registrar o código na descrição (QR forjado também é evidência).
     */
    @Transient
    private String loteInformado;
}