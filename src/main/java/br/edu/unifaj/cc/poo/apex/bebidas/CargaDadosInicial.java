package br.edu.unifaj.cc.poo.apex.bebidas;

import br.edu.unifaj.cc.poo.apex.bebidas.alerta.Alerta;
import br.edu.unifaj.cc.poo.apex.bebidas.alerta.AlertaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.analise.Analise;
import br.edu.unifaj.cc.poo.apex.bebidas.analise.AnaliseRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Destilado;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Fermentado;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.Vinho;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.DestiladoRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.FermentadoRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.bebida.repository.VinhoRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.denuncia.Denuncia;
import br.edu.unifaj.cc.poo.apex.bebidas.denuncia.DenunciaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.Distribuidor;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.PontoVenda;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.repository.DistribuidorRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.distribuidor.repository.PontoVendaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.garrafa.Garrafa;
import br.edu.unifaj.cc.poo.apex.bebidas.garrafa.GarrafaRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.Lote;
import br.edu.unifaj.cc.poo.apex.bebidas.lote.LoteRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Consumidor;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fabricante;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.Fiscal;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.ConsumidorRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.FabricanteRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.pessoa.repository.FiscalRepository;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.Recall;
import br.edu.unifaj.cc.poo.apex.bebidas.recall.RecallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Carga do cenário de demonstração do SafeDrink.
 *
 * Esta é a ÚNICA fonte de dados de demonstração do projeto (o antigo data.sql
 * foi removido). Roda apenas quando o banco está vazio, então reiniciar a
 * aplicação não duplica registros. Para regerar do zero, apague o arquivo
 * "RastreioBebidasDB.mv.db" e suba a aplicação novamente.
 *
 * O cenário foi montado para exercitar os quatro níveis do motor de validação:
 *   LOTE-2024-001 -> ATENCAO  (validade vencida)
 *   LOTE-2024-002 -> SUSPEITO (fabricante irregular, recall, laudo, garrafa falsa, validade)
 *   LOTE-2024-003 -> ATENCAO  (validade vencida)
 *   LOTE-2024-004 -> AUTENTICO (dentro da validade, fabricante regular)
 */
@Component
public class CargaDadosInicial implements CommandLineRunner {

    @Autowired
    FabricanteRepository fabricanteRepository;

    @Autowired
    DistribuidorRepository distribuidorRepository;

    @Autowired
    PontoVendaRepository pontoVendaRepository;

    @Autowired
    VinhoRepository vinhoRepository;

    @Autowired
    DestiladoRepository destiladoRepository;

    @Autowired
    FermentadoRepository fermentadoRepository;

    @Autowired
    LoteRepository loteRepository;

    @Autowired
    GarrafaRepository garrafaRepository;

    @Autowired
    ConsumidorRepository consumidorRepository;

    @Autowired
    FiscalRepository fiscalRepository;

    @Autowired
    DenunciaRepository denunciaRepository;

    @Autowired
    AnaliseRepository analiseRepository;

    @Autowired
    RecallRepository recallRepository;

    @Autowired
    AlertaRepository alertaRepository;

    @Override
    public void run(String... args) {
        if (fabricanteRepository.count() > 0) {
            return;
        }

        // 1. Fabricantes -----------------------------------------------------
        Fabricante alfa = new Fabricante();
        alfa.setNome("Destilaria Alfa");
        alfa.setRazaoSocial("Alfa Bebidas LTDA");
        alfa.setCnpj("11.111.111/0001-11");
        alfa.setCpf("11.111.111/0001-11");
        alfa.setEmail("contato@alfa.com.br");
        alfa.setTelefone("(11) 3333-1111");
        alfa.setDataCadastro("2026-01-15");
        alfa.setRegistroMAPA("MAPA-001");
        alfa.setPaisOrigem("Brasil");
        alfa.setRegular(true);
        alfa = fabricanteRepository.save(alfa);

        Fabricante importadoraX = new Fabricante();
        importadoraX.setNome("Importadora X");
        importadoraX.setRazaoSocial("X Import LTDA");
        importadoraX.setCnpj("22.222.222/0001-22");
        importadoraX.setCpf("22.222.222/0001-22");
        importadoraX.setEmail("contato@ximport.com");
        importadoraX.setTelefone("(11) 3333-2222");
        importadoraX.setDataCadastro("2026-02-10");
        importadoraX.setRegistroMAPA("MAPA-002");
        importadoraX.setPaisOrigem("Paraguai");
        importadoraX.setRegular(false);
        importadoraX = fabricanteRepository.save(importadoraX);

        // 2. Distribuidores --------------------------------------------------
        Distribuidor central = new Distribuidor();
        central.setRazaoSocial("Distribuidora Central LTDA");
        central.setCnpj("33.333.333/0001-33");
        central.setRegiao("Sudeste");
        central.setContato("(11) 3555-1234");
        central = distribuidorRepository.save(central);

        Distribuidor sul = new Distribuidor();
        sul.setRazaoSocial("Distribuidora Sul LTDA");
        sul.setCnpj("44.444.444/0001-44");
        sul.setRegiao("Sul");
        sul.setContato("(41) 3555-5678");
        sul = distribuidorRepository.save(sul);

        // 3. Pontos de venda -------------------------------------------------
        PontoVenda barDoZe = new PontoVenda();
        barDoZe.setNome("Bar do Zé");
        barDoZe.setCnpj("55.555.555/0001-55");
        barDoZe.setEndereco("Rua das Flores, 100 - Jundiaí/SP");
        barDoZe.setTipo("BAR");
        barDoZe = pontoVendaRepository.save(barDoZe);

        PontoVenda mercadoBomPreco = new PontoVenda();
        mercadoBomPreco.setNome("Mercado Bom Preço");
        mercadoBomPreco.setCnpj("66.666.666/0001-66");
        mercadoBomPreco.setEndereco("Av. Central, 500 - Campinas/SP");
        mercadoBomPreco.setTipo("MERCADO");
        mercadoBomPreco = pontoVendaRepository.save(mercadoBomPreco);

        PontoVenda restauranteSabor = new PontoVenda();
        restauranteSabor.setNome("Restaurante Sabor");
        restauranteSabor.setCnpj("77.777.777/0001-77");
        restauranteSabor.setEndereco("Rua XV, 200 - Curitiba/PR");
        restauranteSabor.setTipo("RESTAURANTE");
        restauranteSabor = pontoVendaRepository.save(restauranteSabor);

        // 4. Bebidas ---------------------------------------------------------
        Vinho vinho = new Vinho();
        vinho.setNome("Vinho Tinto Reserva");
        vinho.setMarca("Serra Alta");
        vinho.setUva("Cabernet Sauvignon");
        vinho.setSafra(2020);
        vinho.setRegiao("Serra Gaúcha");
        vinho.setPaisOrigem("Brasil");
        vinho.setTeorAlcoolico(13.5);
        vinho.setVolume(750.0);
        vinho = vinhoRepository.save(vinho);

        Destilado vodka = new Destilado();
        vodka.setNome("Vodka Premium");
        vodka.setMarca("Alfa");
        vodka.setTipoDestilado("VODKA");
        vodka.setAnosEnvelhecimento(0);
        vodka.setPaisOrigem("Brasil");
        vodka.setTeorAlcoolico(40.0);
        vodka.setVolume(1000.0);
        vodka = destiladoRepository.save(vodka);

        Destilado whisky = new Destilado();
        whisky.setNome("Whisky 12 Anos");
        whisky.setMarca("Alfa");
        whisky.setTipoDestilado("WHISKY");
        whisky.setAnosEnvelhecimento(12);
        whisky.setPaisOrigem("Brasil");
        whisky.setTeorAlcoolico(42.0);
        whisky.setVolume(750.0);
        whisky = destiladoRepository.save(whisky);

        Fermentado cerveja = new Fermentado();
        cerveja.setNome("Cerveja Pilsen");
        cerveja.setMarca("Alfa");
        cerveja.setTipoFermentado("CERVEJA");
        cerveja.setFermentacao("BAIXA");
        cerveja.setPaisOrigem("Brasil");
        cerveja.setTeorAlcoolico(5.0);
        cerveja.setVolume(350.0);
        cerveja = fermentadoRepository.save(cerveja);

        // 5. Lotes -----------------------------------------------------------
        Lote lote1 = new Lote();
        lote1.setNumeroLote("LOTE-2024-001");
        lote1.setDataFabricacao("2024-01-15");
        lote1.setDataValidade("2026-01-15");
        lote1.setQuantidade(1000);
        lote1.setSuspeito(false);
        lote1.setFabricante(alfa);
        lote1.setDistribuidor(central);
        lote1.setDestilado(vodka);
        lote1 = loteRepository.save(lote1);

        Lote lote2 = new Lote();
        lote2.setNumeroLote("LOTE-2024-002");
        lote2.setDataFabricacao("2024-03-20");
        lote2.setDataValidade("2026-03-20");
        lote2.setQuantidade(500);
        lote2.setSuspeito(true);
        lote2.setFabricante(importadoraX);
        lote2.setDistribuidor(central);
        lote2.setDestilado(vodka);
        lote2 = loteRepository.save(lote2);

        Lote lote3 = new Lote();
        lote3.setNumeroLote("LOTE-2024-003");
        lote3.setDataFabricacao("2024-05-10");
        lote3.setDataValidade("2026-05-10");
        lote3.setQuantidade(2000);
        lote3.setSuspeito(false);
        lote3.setFabricante(alfa);
        lote3.setDistribuidor(sul);
        lote3.setFermentado(cerveja);
        lote3 = loteRepository.save(lote3);

        Lote lote4 = new Lote();
        lote4.setNumeroLote("LOTE-2024-004");
        lote4.setDataFabricacao("2024-06-01");
        lote4.setDataValidade("2028-06-01");
        lote4.setQuantidade(300);
        lote4.setSuspeito(false);
        lote4.setFabricante(alfa);
        lote4.setDistribuidor(sul);
        lote4.setVinho(vinho);
        lote4 = loteRepository.save(lote4);

        // 6. Garrafas (selo digital) -----------------------------------------
        Garrafa g1 = new Garrafa();
        g1.setCodigoBarras("7891234567890");
        g1.setSeloFiscal("SELO-001");
        g1.setStatus("REGULAR");
        g1.setLote(lote1);
        g1.setPontoVenda(barDoZe);
        garrafaRepository.save(g1);

        Garrafa g7 = new Garrafa();
        g7.setCodigoBarras("7891234567891");
        g7.setCodigoUnico("GAR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        g7.setContadorVerificacoes(0);
        garrafaRepository.save(g7);

        Garrafa g2 = new Garrafa();
        g2.setCodigoBarras("7891234567891");
        g2.setSeloFiscal("SELO-002");
        g2.setStatus("REGULAR");
        g2.setLote(lote1);
        g2.setPontoVenda(mercadoBomPreco);
        garrafaRepository.save(g2);

        Garrafa g3 = new Garrafa();
        g3.setCodigoBarras("7891234567892");
        g3.setSeloFiscal("SELO-FALSO-001");
        g3.setStatus("SUSPEITA");
        g3.setLote(lote2);
        g3.setPontoVenda(barDoZe);
        garrafaRepository.save(g3);

        Garrafa g4 = new Garrafa();
        g4.setCodigoBarras("7891234567893");
        g4.setSeloFiscal("SELO-FALSO-002");
        g4.setStatus("FALSIFICADA");
        g4.setLote(lote2);
        g4.setPontoVenda(mercadoBomPreco);
        garrafaRepository.save(g4);

        Garrafa g5 = new Garrafa();
        g5.setCodigoBarras("7891234567894");
        g5.setSeloFiscal("SELO-005");
        g5.setStatus("REGULAR");
        g5.setLote(lote3);
        g5.setPontoVenda(restauranteSabor);
        garrafaRepository.save(g5);

        Garrafa g6 = new Garrafa();
        g6.setCodigoBarras("7891234567895");
        g6.setSeloFiscal("SELO-006");
        g6.setStatus("REGULAR");
        g6.setLote(lote4);
        g6.setPontoVenda(restauranteSabor);
        garrafaRepository.save(g6);

        // 7. Consumidores e fiscais ------------------------------------------
        Consumidor joao = new Consumidor();
        joao.setNome("João Silva");
        joao.setCpf("111.222.333-44");
        joao.setEmail("joao@email.com");
        joao.setTelefone("(11) 91234-5678");
        joao.setDataCadastro("2026-01-10");
        joao.setCidade("Jundiaí");
        joao.setEstado("SP");
        joao = consumidorRepository.save(joao);

        Consumidor maria = new Consumidor();
        maria.setNome("Maria Santos");
        maria.setCpf("222.333.444-55");
        maria.setEmail("maria@email.com");
        maria.setTelefone("(19) 92345-6789");
        maria.setDataCadastro("2026-02-20");
        maria.setCidade("Campinas");
        maria.setEstado("SP");
        maria = consumidorRepository.save(maria);

        Consumidor carlos = new Consumidor();
        carlos.setNome("Carlos Oliveira");
        carlos.setCpf("333.444.555-66");
        carlos.setEmail("carlos@email.com");
        carlos.setTelefone("(41) 93456-7890");
        carlos.setDataCadastro("2026-03-15");
        carlos.setCidade("Curitiba");
        carlos.setEstado("PR");
        carlos = consumidorRepository.save(carlos);

        Fiscal ana = new Fiscal();
        ana.setNome("Ana Costa");
        ana.setCpf("444.555.666-77");
        ana.setEmail("ana@mapa.gov.br");
        ana.setTelefone("(11) 3888-1000");
        ana.setDataCadastro("2026-01-05");
        ana.setMatricula("FISC-001");
        ana.setOrgao("MAPA");
        ana.setRegiaoAtuacao("Sudeste");
        ana = fiscalRepository.save(ana);

        Fiscal pedro = new Fiscal();
        pedro.setNome("Pedro Lima");
        pedro.setCpf("555.666.777-88");
        pedro.setEmail("pedro@anvisa.gov.br");
        pedro.setTelefone("(41) 3888-2000");
        pedro.setDataCadastro("2026-01-12");
        pedro.setMatricula("FISC-002");
        pedro.setOrgao("ANVISA");
        pedro.setRegiaoAtuacao("Sul");
        pedro = fiscalRepository.save(pedro);

        // 8. Denúncias -------------------------------------------------------
        // Cada denúncia aponta para a garrafa reclamada E para o lote dela:
        // é o lote que permite à fiscalização bloquear o restante da produção.
        Denuncia denuncia1 = new Denuncia();
        denuncia1.setProtocolo("SD-2026-DEMO01");
        denuncia1.setData("2026-06-10");
        denuncia1.setDescricao("Sabor estranho e dor de cabeça após consumo");
        denuncia1.setStatus("CONFIRMADA");
        denuncia1.setConsumidor(joao);
        denuncia1.setGarrafa(g4);
        denuncia1.setLote(lote2);
        denuncia1 = denunciaRepository.save(denuncia1);

        Denuncia denuncia2 = new Denuncia();
        denuncia2.setProtocolo("SD-2026-DEMO02");
        denuncia2.setData("2026-06-15");
        denuncia2.setDescricao("Cheiro forte de álcool diferente do normal");
        denuncia2.setStatus("EM_ANALISE");
        denuncia2.setConsumidor(maria);
        denuncia2.setGarrafa(g3);
        denuncia2.setLote(lote2);
        denuncia2 = denunciaRepository.save(denuncia2);

        // 9. Laudos periciais ------------------------------------------------
        Analise analise1 = new Analise();
        analise1.setData("2026-06-18");
        analise1.setResultado("FALSIFICADA");
        analise1.setLaudo("Presença de metanol acima do permitido");
        analise1.setLaboratorio("LabCentral");
        analise1.setFiscal(ana);
        analise1.setDenuncia(denuncia1);
        analiseRepository.save(analise1);

        Analise analise2 = new Analise();
        analise2.setData("2026-06-22");
        analise2.setResultado("ADULTERADA");
        analise2.setLaudo("Diluição com água acima do permitido");
        analise2.setLaboratorio("LabSul");
        analise2.setFiscal(pedro);
        analise2.setDenuncia(denuncia2);
        analiseRepository.save(analise2);

        // 10. Recall ---------------------------------------------------------
        Recall recall = new Recall();
        recall.setDataInicio("2026-06-20");
        recall.setDataFim(null);
        recall.setMotivo("Lote com metanol - risco à saúde");
        recall.setStatus("ATIVO");
        recall.setLote(lote2);
        recall = recallRepository.save(recall);

        // 11. Alertas --------------------------------------------------------
        Alerta alerta1 = new Alerta();
        alerta1.setData("2026-06-20");
        alerta1.setNivel("ALTO");
        alerta1.setRegiao("Sudeste");
        alerta1.setRecall(recall);
        alertaRepository.save(alerta1);

        Alerta alerta2 = new Alerta();
        alerta2.setData("2026-06-20");
        alerta2.setNivel("MEDIO");
        alerta2.setRegiao("Sul");
        alerta2.setRecall(recall);
        alertaRepository.save(alerta2);

        System.out.println(">>> [SafeDrink] Cenario de demonstracao carregado: "
                + "4 lotes (1 autentico, 2 em atencao, 1 suspeito), 6 garrafas, 2 laudos e 1 recall.");
    }
}
