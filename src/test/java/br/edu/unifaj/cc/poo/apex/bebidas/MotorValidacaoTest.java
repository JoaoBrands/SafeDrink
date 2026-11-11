package br.edu.unifaj.cc.poo.apex.bebidas;

import br.edu.unifaj.cc.poo.apex.bebidas.analise.AnaliseRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.denuncia.DenunciaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.garrafa.Garrafa;
import br.edu.unifaj.cc.poo.apex.bebidas.garrafa.GarrafaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.Lote;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.LoteRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fabricante;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.Recall;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.RecallRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.validacao.MotorValidacao;
import br.edu.unifaj.cc.poo.apex.bebidas.validacao.ResultadoValidacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MotorValidacaoTest {

    @Mock
    private LoteRepository loteRepository;

    @Mock
    private GarrafaRepository garrafaRepository;

    @Mock
    private RecallRepository recallRepository;

    @Mock
    private DenunciaRepository denunciaRepository;

    @Mock
    private AnaliseRepository analiseRepository;

    @InjectMocks
    private MotorValidacao motorValidacao;

    private Garrafa garrafaBase;
    private Lote loteBase;
    private Fabricante fabricanteBase;

    @BeforeEach
    void setup() {
        fabricanteBase = new Fabricante();
        fabricanteBase.setId(1);
        fabricanteBase.setNome("Vinícola Teste");
        fabricanteBase.setRegular(true);

        loteBase = new Lote();
        loteBase.setId(10);
        loteBase.setNumeroLote("LOT-2026-001");
        loteBase.setFabricante(fabricanteBase);
        loteBase.setDataValidade(LocalDate.now().plusMonths(6).toString());
        loteBase.setSuspeito(false);

        garrafaBase = new Garrafa();
        garrafaBase.setId(100);
        garrafaBase.setCodigoBarras("7891234567890");
        garrafaBase.setSeloFiscal("SEL-8899");
        garrafaBase.setCodigoUnico("GAR-A1B2C3D4");
        garrafaBase.setStatus("AUTENTICO");
        garrafaBase.setContadorVerificacoes(0);
        garrafaBase.setLote(loteBase);
    }

    @Test
    @DisplayName("1. Deve validar garrafa autêntica e retornar nível AUTENTICO")
    void validarGarrafa_Autentica_DeveRetornarAutentico() {
        // Arrange
        when(garrafaRepository.save(any(Garrafa.class))).thenAnswer(i -> i.getArgument(0));
        when(recallRepository.findByLoteId(10)).thenReturn(Collections.emptyList());
        when(denunciaRepository.findByLoteOuGarrafaDoLote(10)).thenReturn(Collections.emptyList());

        // Act
        ResultadoValidacao resultado = motorValidacao.validarGarrafa(garrafaBase);

        // Assert
        assertNotNull(resultado);
        assertEquals(ResultadoValidacao.AUTENTICO, resultado.getNivel());
        assertEquals("Selo de rastreabilidade conforme", resultado.getTitulo());
        assertEquals(1, garrafaBase.getContadorVerificacoes());
        verify(garrafaRepository, times(1)).save(garrafaBase);
    }

    @Test
    @DisplayName("2. Deve sinalizar ATENÇÃO para garrafa com status SUSPEITA")
    void validarGarrafa_StatusSuspeita_DeveRetornarAtencao() {
        // Arrange
        garrafaBase.setStatus("SUSPEITA");
        when(garrafaRepository.save(any(Garrafa.class))).thenAnswer(i -> i.getArgument(0));
        when(recallRepository.findByLoteId(10)).thenReturn(Collections.emptyList());

        // Act
        ResultadoValidacao resultado = motorValidacao.validarGarrafa(garrafaBase);

        // Assert
        assertEquals(ResultadoValidacao.ATENCAO, resultado.getNivel());
        assertTrue(resultado.getMotivos().stream()
                .anyMatch(m -> m.contains("sinalizada como suspeita pela fiscalização")));
    }

    @Test
    @DisplayName("3. Deve gerar alerta de clonagem quando a garrafa for consultada mais de 3 vezes")
    void validarGarrafa_MaisDeTresConsultas_DeveRetornarSuspeito() {
        // Arrange
        garrafaBase.setContadorVerificacoes(3); // 3a consulta anterior -> vai para 4a consulta agora
        when(garrafaRepository.save(any(Garrafa.class))).thenAnswer(i -> i.getArgument(0));
        when(recallRepository.findByLoteId(10)).thenReturn(Collections.emptyList());

        // Act
        ResultadoValidacao resultado = motorValidacao.validarGarrafa(garrafaBase);

        // Assert
        assertEquals(ResultadoValidacao.SUSPEITO, resultado.getNivel());
        assertEquals(4, garrafaBase.getContadorVerificacoes());
        assertTrue(resultado.getMotivos().stream()
                .anyMatch(m -> m.contains("Alerta de clonagem") || m.contains("4 vezes")));
    }

    @Test
    @DisplayName("4. Deve rejeitar lote/garrafa vinculada a Recall em aberto")
    void validarGarrafa_LoteComRecall_DeveRetornarSuspeito() {
        // Arrange
        Recall recall = new Recall();
        recall.setId(1);
        recall.setMotivo("Contaminação por corpo estranho");
        recall.setStatus("ATIVO");

        when(garrafaRepository.save(any(Garrafa.class))).thenAnswer(i -> i.getArgument(0));
        when(recallRepository.findByLoteId(10)).thenReturn(List.of(recall));

        // Act
        ResultadoValidacao resultado = motorValidacao.validarGarrafa(garrafaBase);

        // Assert
        assertEquals(ResultadoValidacao.SUSPEITO, resultado.getNivel());
        assertTrue(resultado.getMotivos().stream()
                .anyMatch(m -> m.contains("Recall aberto para este lote")));
    }

    @Test
    @DisplayName("5. Deve retornar Registro Inexistente quando buscar lote por chave invalida")
    void validarPorChave_LoteInexistente_DeveRetornarNaoEncontrado() {
        // Arrange
        when(loteRepository.buscarPorChave("INEXISTENTE")).thenReturn(Optional.empty());

        // Act
        ResultadoValidacao resultado = motorValidacao.validarPorChave("INEXISTENTE");

        // Assert
        assertNotNull(resultado);
        assertEquals(ResultadoValidacao.NAO_ENCONTRADO, resultado.getNivel());
        assertEquals("Registro inexistente", resultado.getTitulo());
    }
}