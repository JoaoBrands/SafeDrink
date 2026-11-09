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

        // 1. Fabricantes
        Fabricante fab1 = new Fabricante();
        fab1.setNome("Vinícola Vale dos Vinhedos S/A");
        fab1.setCpf("000.000.000-00");
        fab1.setEmail("contato@valedosvinhedos.com.br");
        fab1.setTelefone("(54) 3456-7890");
        fab1.setDataCadastro("2024-01-15");
        fab1.setCnpj("12.345.678/0001-90");
        fab1.setRazaoSocial("Vinícola Vale dos Vinhedos S/A");
        fab1.setRegistroMAPA("RS-01234-5");
        fab1.setPaisOrigem("Brasil");
        fab1.setRegular(true);
        fab1 = fabricanteRepository.save(fab1);

        Fabricante fab2 = new Fabricante();
        fab2.setNome("Bebidas Clandestinas do Sul Ltda");
        fab2.setCpf("111.111.111-11");
        fab2.setEmail("contato@bebidasdosul.com");
        fab2.setTelefone("(11) 98765-4321");
        fab2.setDataCadastro("2024-03-10");
        fab2.setCnpj("98.765.432/0001-10");
        fab2.setRazaoSocial("Bebidas Clandestinas do Sul Ltda");
        fab2.setRegistroMAPA("SP-99999-0");
        fab2.setPaisOrigem("Brasil");
        fab2.setRegular(false);
        fab2 = fabricanteRepository.save(fab2);

        // 2. Distribuidores
        Distribuidor dist = new Distribuidor();
        dist.setCnpj("33.444.555/0001-22");
        dist.setRazaoSocial("Logística Express de Bebidas S/A");
        dist.setRegiao("Sudeste");
        dist.setContato("operacoes@logexpress.com.br");
        dist = distribuidorRepository.save(dist);

        // 3. Pontos de Venda
        PontoVenda pv = new PontoVenda();
        pv.setCnpj("44.555.666/0001-33");
        pv.setNome("Empório & Adega Santa Cecília");
        pv.setEndereco("Av. Paulista, 1500, São Paulo - SP");
        pv.setTipo("Adega / Supermercado");
        pv.setDistribuidor(dist);
        pv = pontoVendaRepository.save(pv);

        // 4. Bebidas
        Vinho vinho = new Vinho();
        vinho.setNome("Cabernet Sauvignon Reserva Especial");
        vinho.setMarca("Vale Imperial");
        vinho.setTeorAlcoolico(13.5);
        vinho.setVolume(750.0);
        vinho.setPaisOrigem("Brasil");
        vinho.setUva("Cabernet Sauvignon");
        vinho.setSafra(2020);
        vinho.setRegiao("Serra Gaúcha");
        vinho = vinhoRepository.save(vinho);

        Destilado destilado = new Destilado();
        destilado.setNome("Whisky Single Malt 12 Anos");
        destilado.setMarca("Highland Crown");
        destilado.setTeorAlcoolico(40.0);
        destilado.setVolume(1000.0);
        destilado.setPaisOrigem("Escócia");
        destilado.setTipoDestilado("Whisky");
        destilado.setAnosEnvelhecimento(12);
        destilado = destiladoRepository.save(destilado);

        Fermentado fermentado = new Fermentado();
        fermentado.setNome("Cerveja Artesanal IPA Puro Malte");
        fermentado.setMarca("BrewMaster");
        fermentado.setTeorAlcoolico(6.5);
        fermentado.setVolume(500.0);
        fermentado.setPaisOrigem("Brasil");
        fermentado.setTipoFermentado("IPA");
        fermentado.setFermentacao("Alta Fermentação (Ale)");
        fermentado = fermentadoRepository.save(fermentado);

        // 5. Lotes
        // Lote 1: Regular / Autêntico
        Lote lote1 = new Lote();
        lote1.setNumeroLote("LOT-2026-AUT01");
        lote1.setDataFabricacao("2026-02-10");
        lote1.setDataValidade("2030-02-10");
        lote1.setQuantidade(1000);
        lote1.setSuspeito(false);
        lote1.setFabricante(fab1);
        lote1.setDistribuidor(dist);
        lote1.setVinho(vinho);
        lote1 = loteRepository.save(lote1);

        // Lote 2: Suspeito / Alvo de Fraude
        Lote lote2 = new Lote();
        lote2.setNumeroLote("LOT-2026-FAL02");
        lote2.setDataFabricacao("2026-03-01");
        lote2.setDataValidade("2027-03-01");
        lote2.setQuantidade(500);
        lote2.setSuspeito(true);
        lote2.setFabricante(fab2);
        lote2.setDistribuidor(dist);
        lote2.setDestilado(destilado);
        lote2 = loteRepository.save(lote2);

        // 6. Garrafas
        Garrafa g1 = new Garrafa();
        g1.setCodigoBarras("789123456001");
        g1.setDataEnvase("2026-02-12");
        g1.setSeloFiscal("SELO-BR-984321-OK");
        g1.setStatus("AUTENTICA");
        g1.setLote(lote1);
        g1.setPontoVenda(pv);
        garrafaRepository.save(g1);

        Garrafa g2 = new Garrafa();
        g2.setCodigoBarras("789123456002");
        g2.setDataEnvase("2026-03-05");
        g2.setSeloFiscal("SELO-CLONADO-001");
        g2.setStatus("FALSIFICADA");
        g2.setLote(lote2);
        g2.setPontoVenda(pv);
        garrafaRepository.save(g2);

        // 7. Consumidor & Fiscal
        Consumidor cons = new Consumidor();
        cons.setNome("Carlos Eduardo Silva");
        cons.setCpf("123.456.789-00");
        cons.setEmail("carlos.silva@email.com");
        cons.setTelefone("(19) 99888-7766");
        cons.setDataCadastro("2026-01-20");
        cons.setEndereco("Rua das Flores, 120");
        cons.setCidade("Jaguariúna");
        cons.setEstado("SP");
        cons = consumidorRepository.save(cons);

        Fiscal fiscal = new Fiscal();
        fiscal.setNome("Mariana Costa Oliveira");
        fiscal.setCpf("321.654.987-11");
        fiscal.setEmail("fiscal.mariana@mapa.gov.br");
        fiscal.setTelefone("(19) 3888-1234");
        fiscal.setDataCadastro("2024-05-10");
        fiscal.setMatricula("MAPA-8849");
        fiscal.setOrgao("MAPA / Ministério da Agricultura");
        fiscal.setRegiaoAtuacao("Região Metropolitana de Campinas");
        fiscal = fiscalRepository.save(fiscal);

        // 8. Denúncia
        Denuncia denuncia = new Denuncia();
        denuncia.setData("2026-03-15");
        denuncia.setDescricao("Garrafa com tampa adulterada, odor de solvente químico e selo fiscal borrado.");
        denuncia.setStatus("PROCEDENTE");
        denuncia.setConsumidor(cons);
        denuncia.setGarrafa(g2);
        denuncia = denunciaRepository.save(denuncia);

        // 9. Análise Pericial
        Analise analise = new Analise();
        analise.setData("2026-03-18");
        analise.setResultado("ADULTERADA");
        analise.setLaudo("Identificada presença de metanol acima dos limites legais permitidos e corante artificial não autorizado.");
        analise.setLaboratorio("Laboratório Central de Análises Toxicológicas - LACAT");
        analise.setFiscal(fiscal);
        analise.setDenuncia(denuncia);
        analise = analiseRepository.save(analise);

        // 10. Recall
        Recall recall = new Recall();
        recall.setDataInicio("2026-03-20");
        recall.setDataFim("2026-04-20");
        recall.setMotivo("Contaminação e adulteração com metanol no lote LOT-2026-FAL02.");
        recall.setStatus("EM_ANDAMENTO");
        recall.setLote(lote2);
        recall = recallRepository.save(recall);

        // 11. Alerta
        Alerta alerta = new Alerta();
        alerta.setData("2026-03-20");
        alerta.setNivel("GRAVE / CRÍTICO");
        alerta.setRegiao("Sudeste");
        alerta.setRecall(recall);
        alertaRepository.save(alerta);

        System.out.println(">>> [SafeDrink] Carga inicial de dados de demonstração concluída com sucesso!");
    }
}
