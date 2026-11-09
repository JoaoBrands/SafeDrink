package br.edu.unifaj.cc.poo.apex.bebidas.alerta;

import br.edu.unifaj.cc.poo.apex.bebidas.recall.Recall;
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
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String data;
    private String nivel;
    private String regiao;

    @ManyToOne
    @JoinColumn(name = "recall_id")
    private Recall recall;
}