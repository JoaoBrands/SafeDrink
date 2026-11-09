-- ============================================================
-- SafeDrink - Script de Demonstração e Carga Inicial de Dados
-- ============================================================

-- Fabricantes
INSERT INTO fabricante (id, nome, cpf, email, telefone, data_cadastro, cnpj, razao_social, registro_mapa, pais_origem, regular)
VALUES (1, 'Vinícola Vale dos Vinhedos S/A', '000.000.000-00', 'contato@valedosvinhedos.com.br', '(54) 3456-7890', '2024-01-15', '12.345.678/0001-90', 'Vinícola Vale dos Vinhedos S/A', 'RS-01234-5', 'Brasil', TRUE);

INSERT INTO fabricante (id, nome, cpf, email, telefone, data_cadastro, cnpj, razao_social, registro_mapa, pais_origem, regular)
VALUES (2, 'Bebidas Clandestinas do Sul Ltda', '111.111.111-11', 'contato@bebidasdosul.com', '(11) 98765-4321', '2024-03-10', '98.765.432/0001-10', 'Bebidas Clandestinas do Sul Ltda', 'SP-99999-0', 'Brasil', FALSE);

-- Distribuidores
INSERT INTO distribuidor (id, cnpj, razao_social, regiao, contato)
VALUES (1, '33.444.555/0001-22', 'Logística Express de Bebidas S/A', 'Sudeste', 'operacoes@logexpress.com.br');

-- Pontos de Venda
INSERT INTO ponto_venda (id, cnpj, nome, endereco, tipo, distribuidor_id)
VALUES (1, '44.555.666/0001-33', 'Empório & Adega Santa Cecília', 'Av. Paulista, 1500, São Paulo - SP', 'Adega / Supermercado', 1);

-- Bebidas
INSERT INTO vinho (id, nome, marca, teor_alcoolico, volume, pais_origem, uva, safra, regiao)
VALUES (1, 'Cabernet Sauvignon Reserva Especial', 'Vale Imperial', 13.5, 750.0, 'Brasil', 'Cabernet Sauvignon', 2020, 'Serra Gaúcha');

INSERT INTO destilado (id, nome, marca, teor_alcoolico, volume, pais_origem, tipo_destilado, anos_envelhecimento)
VALUES (1, 'Whisky Single Malt 12 Anos', 'Highland Crown', 40.0, 1000.0, 'Escócia', 'Whisky', 12);

INSERT INTO fermentado (id, nome, marca, teor_alcoolico, volume, pais_origem, tipo_fermentado, fermentacao)
VALUES (1, 'Cerveja Artesanal IPA Puro Malte', 'BrewMaster', 6.5, 500.0, 'Brasil', 'IPA', 'Alta Fermentação (Ale)');

-- Lotes
INSERT INTO lote (id, numero_lote, data_fabricacao, data_validade, quantidade, suspeito, fabricante_id, distribuidor_id, vinho_id)
VALUES (1, 'LOT-2026-AUT01', '2026-02-10', '2030-02-10', 1000, FALSE, 1, 1, 1);

INSERT INTO lote (id, numero_lote, data_fabricacao, data_validade, quantidade, suspeito, fabricante_id, distribuidor_id, destilado_id)
VALUES (2, 'LOT-2026-FAL02', '2026-03-01', '2027-03-01', 500, TRUE, 2, 1, 1);

-- Garrafas
INSERT INTO garrafa (id, codigo_barras, data_envase, selo_fiscal, status, lote_id, ponto_venda_id)
VALUES (1, '789123456001', '2026-02-12', 'SELO-BR-984321-OK', 'AUTENTICA', 1, 1);

INSERT INTO garrafa (id, codigo_barras, data_envase, selo_fiscal, status, lote_id, ponto_venda_id)
VALUES (2, '789123456002', '2026-03-05', 'SELO-CLONADO-001', 'FALSIFICADA', 2, 1);

-- Consumidor e Fiscal
INSERT INTO consumidor (id, nome, cpf, email, telefone, data_cadastro, endereco, cidade, estado)
VALUES (1, 'Carlos Eduardo Silva', '123.456.789-00', 'carlos.silva@email.com', '(19) 99888-7766', '2026-01-20', 'Rua das Flores, 120', 'Jaguariúna', 'SP');

INSERT INTO fiscal (id, nome, cpf, email, telefone, data_cadastro, matricula, orgao, regiao_atuacao)
VALUES (1, 'Mariana Costa Oliveira', '321.654.987-11', 'fiscal.mariana@mapa.gov.br', '(19) 3888-1234', '2024-05-10', 'MAPA-8849', 'MAPA / Ministério da Agricultura', 'Região Metropolitana de Campinas');

-- Denúncia
INSERT INTO denuncia (id, data, descricao, status, consumidor_id, garrafa_id)
VALUES (1, '2026-03-15', 'Garrafa com tampa adulterada, odor de solvente químico e selo fiscal borrado.', 'PROCEDENTE', 1, 2);

-- Análise
INSERT INTO analise (id, data, resultado, laudo, laboratorio, fiscal_id, denuncia_id)
VALUES (1, '2026-03-18', 'ADULTERADA', 'Identificada presença de metanol acima dos limites legais permitidos e corante artificial não autorizado.', 'Laboratório Central de Análises Toxicológicas - LACAT', 1, 1);

-- Recall
INSERT INTO recall (id, data_inicio, data_fim, motivo, status, lote_id)
VALUES (1, '2026-03-20', '2026-04-20', 'Contaminação e adulteração com metanol no lote LOT-2026-FAL02.', 'EM_ANDAMENTO', 2);

-- Alerta
INSERT INTO alerta (id, data, nivel, regiao, recall_id)
VALUES (1, '2026-03-20', 'GRAVE / CRÍTICO', 'Sudeste', 1);
