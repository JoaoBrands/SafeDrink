# SafeDrink — Rastreio e validação de bebidas

Sistema para o consumidor verificar se uma bebida é legítima, denunciar suspeitas e
ser avisado quando um lote que ele verificou entrar em recall. A fiscalização opera
um painel administrativo protegido por chave.

> ⚠️ **Dados fictícios.** Todos os fabricantes, lotes, laudos e recalls deste
> repositório são inventados para demonstração acadêmica. O sistema **não** substitui
> os canais oficiais do MAPA, ANVISA, Procon ou da vigilância sanitária.

---

## 1. O problema e a proposta

Bebidas falsificadas (metanol, diluição, selo clonado) matam. O consumidor, na hora
da compra, não tem como saber se o selo da garrafa é verdadeiro.

O SafeDrink cobre o ciclo completo:

| Ator | O que faz | Onde |
|---|---|---|
| **Consumidor** | Consulta o lote/selo, denuncia, acompanha o protocolo, recebe alerta de recall | Aplicativo Android |
| **Fiscalização** | Cadastra fabricante, lote, garrafa, laudo e abre recall | Painel web (com chave) |
| **Sistema** | Cruza fabricante, recall, laudo, garrafas e validade e devolve um veredito com motivos | Backend |

---

## 2. Stack

- **Backend:** Java 17 · Spring Boot 4.0.8 · Spring Data JPA · H2 (arquivo) · Lombok
- **Frontend web:** HTML + CSS + JavaScript puro (`fetch`)
- **Mobile:** Android (Java) · Volley · ZXing (QR Code) · SharedPreferences · Notification

---

## 3. Como rodar o backend

```bash
# na raiz do projeto
./mvnw spring-boot:run      # Linux/macOS
mvnw.cmd spring-boot:run    # Windows
```

Abre em <http://localhost:8080>. O painel fica em `/index.html`, o portal público do
consumidor em `/validar.html`.

O banco é um arquivo `RastreioBebidasDB.mv.db` criado na raiz. Na **primeira execução**
o `CargaDadosInicial` popula o cenário de demonstração.

**Para voltar ao estado original:** pare a aplicação, apague `RastreioBebidasDB.mv.db`
e suba de novo. O banco **não é versionado** (está no `.gitignore`).

---

## 4. Chave de escrita (fiscalização)

O painel administrativo pede uma chave na primeira operação de cada sessão do navegador.

```
Chave: FISCAL-2026
Cabeçalho HTTP: X-Chave-Escrita
Configuração: application.properties → safedrink.chave-escrita
```

> Troque essa chave antes de apresentar. Ela é um **controle de demonstração**, não
> autenticação: é uma chave compartilhada, sem usuário nem perfil.

---

## 5. Endpoints

### Públicos (consumidor)

| Método | Endpoint | Para quê |
|---|---|---|
| GET | `/api/bebidas/validacao/lote/{id ou número}` | Veredito do lote (aceita `1` ou `LOTE-2024-002`) |
| GET | `/api/bebidas/validacao/codigo-barras/{ean}` | Veredito por garrafa/selo |
| GET | `/api/bebidas/aviso` | Mural de recalls e alertas |
| GET | `/api/bebidas/lote/{id}` | Leitura de um lote (usada ao denunciar) |
| POST | `/api/bebidas/denuncia` | Registrar denúncia (servidor define data, status e protocolo; aceita `loteInformado` com o ID ou número lido, e anota o código na descrição se o lote não existir) |
| GET | `/api/bebidas/denuncia/protocolo/{protocolo}` | Acompanhar a denúncia (sem dados pessoais) |
| GET | `/api/bebidas/external/tributacao/{ean}` | Consulta fiscal do produto (NCM, CEST) |
| GET | `/api/bebidas/external/imagem/{ean}` | Foto do produto por código de barras |

### Protegidos (exigem `X-Chave-Escrita`)

Fabricante, Lote (exceto a leitura por id), Garrafa, Análise, Recall, **Consumidor** e
**Fiscal** (todas as operações, pois têm dados pessoais), as operações de
listagem/alteração/exclusão de Denúncia e a **escrita** (POST/PUT/DELETE) de Vinho,
Destilado, Fermentado, Distribuidor, Ponto de Venda e Alerta (a leitura desses
cadastros continua pública).

### Ainda abertos (dívida técnica assumida)

Apenas `Relatorio` (estatísticas agregadas, sem dados pessoais) e a leitura dos cadastros
de bebida, distribuidor, ponto de venda e alerta.

---

## 6. Aplicativo Android

Projeto em `E:\Programacao\Faculdade\2026\ApexBebidas` (fora deste repositório).

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Tela inicial → campo "Endereço do servidor":** no emulador, `http://10.0.2.2:8080`;
no celular físico, `http://SEU_IP_LOCAL:8080` (mesma rede Wi-Fi).

> Em um dos emuladores testados, `10.0.2.2` **não roteou** e foi preciso usar o IP da
> máquina. Teste o endereço antes da apresentação.

Telas: **Validar** (QR Code ou número do lote) · **Denunciar** · **Avisos e recalls**
(agrupados por lote) · **Acompanhar denúncia** (por protocolo).

O app guarda em `SharedPreferences` o histórico dos lotes verificados e **notifica**
quando um deles entra em recall. A permissão `POST_NOTIFICATIONS` é pedida na primeira
abertura (Android 13+).

---

## 7. Dados de demonstração

### Lotes (veredito esperado)

| Lote | Veredito | Por quê |
|---|---|---|
| `LOTE-2024-001` | ⚠️ ATENÇÃO | validade vencida em 15/01/2026 |
| `LOTE-2024-002` | 🚨 SUSPEITO | fabricante irregular + recall + laudo + garrafa falsificada + validade |
| `LOTE-2024-003` | ⚠️ ATENÇÃO | validade vencida |
| `LOTE-2024-004` | ✅ AUTÊNTICO | fabricante regular, sem recall, dentro da validade |

### Protocolos de denúncia

| Protocolo | Status | Laudo |
|---|---|---|
| `SD-2026-DEMO01` | CONFIRMADA | FALSIFICADA |
| `SD-2026-DEMO02` | EM_ANALISE | ADULTERADA |

### Garrafas (EAN → status)

`7891234567890/91` REGULAR · `7891234567892` SUSPEITA · **`7891234567893` FALSIFICADA** ·
`7891234567894/95` REGULAR

### Consulta externa de produto

EAN de teste: **`78905351`** → "CERVEJA ORIGINAL 600ML", NCM 22030000, CEST 0302100.

---

## 8. Roteiro de apresentação (~7 min)

1. **O problema** (30 s): bebida falsificada mata; o selo não é verificável hoje.
2. **`validar.html`** — digite `LOTE-2024-002`: 6 motivos objetivos aparecem.
   Depois `LOTE-2024-004`: autêntico. *(Mostra que não é um "sim/não" arbitrário.)*
3. **App** — valide `LOTE-2024-002` pelo QR/número: mesma resposta no celular.
4. **App** — toque em "Denunciar", envie e mostre o **protocolo** gerado pelo servidor.
5. **App** — "Acompanhar denúncia": consulte `SD-2026-DEMO01` e veja o laudo.
6. **App** — "Avisos e recalls": os avisos aparecem **agrupados por lote**.
7. **O "uau"** — valide `LOTE-2024-004` no app; no painel (`recall.html`), crie um recall
   para o lote 4 informando a chave; volte ao app: **a notificação chega**.
8. **Segurança** — tente excluir um lote pelo painel sem a chave: bloqueado. Depois com
   a chave: funciona. *(O registro oficial não é público.)*
9. **Consulta EAN** — em `bebidas.html`, busque `78905351` e mostre descrição, NCM,
   CEST e a foto do produto vindos da API de Produtos.

---

## 9. Alinhamento com a ementa

| Conceito da aula | Onde está |
|---|---|
| JS OO, funções, objetos (POO II A2) | `static/acesso.js`, scripts das páginas |
| `fetch`, REST, `@RestController`, `@RequestBody`, classe `Retorno` (A3) | todas as páginas e controllers |
| `@Repository`/`JpaRepository`, `@Autowired` (A4, A6) | `*.repository`, controllers |
| CRUD, `throw`, mensagem amigável no `Retorno` (A5) | controllers + `AcessoNegadoException` |
| `@Entity`, `@Table`, `@Id`, `@Column`, `@Query`, `ManyToOne`/`OneToMany` (A6) | pacotes de entidade |
| Singleton + Façade (A9) | `ControleAcesso` (singleton), `DenunciaService` e `MotorValidacao` (fachada) |
| Activity, Intent (Móvel A3) | `MainActivity` → demais telas |
| `Toast`, `Notification` (Móvel A4) | `NotificadorRecall`, avisos de tela |
| `SharedPreferences` (Móvel A6) | `Config`, `HistoricoVerificacoes` |
| XML e JSON (Móvel A7) | `org.json` (`JSONObject`/`JSONArray`) |
| REST com Volley (Móvel A8) | `Utf8StringRequest` + Volley |

**Não utilizados (e por quê):**
- **Herança/polimorfismo** — `Fabricante`/`Consumidor`/`Fiscal` e `Vinho`/`Destilado`/
  `Fermentado` são classes independentes, com atributos repetidos. Uma superclasse
  `Pessoa` e `Bebida` seria o caminho natural, mas ficou fora do escopo combinado.
- **Padrões Factory Method, Observer, Template Method** (A9) — estudados, não aplicados.
- **Streams e serialização Java** (A8 POO II) — o projeto usa JSON em vez de arquivos.
- **RecyclerView** (Móvel A4) — as listas usam `LinearLayout` + `inflate`.

---

## 10. Limitações conhecidas

- A chave de escrita é **compartilhada** e fica no `application.properties` (versionado).
- `Retorno` devolve sempre HTTP 200; e mensagens de sucesso usam `status: "Erro"`.
- Datas são `String` (`"2026-06-10"`), não `LocalDate`; não há `@Valid` nem `@NotBlank`.
- **LGPD:** CPF, e-mail e telefone são coletados sem consentimento (o acesso à listagem
  agora exige a chave, mas não há política de retenção nem consentimento).
- Existe apenas 1 teste automatizado (`contextLoads`).

---

## 11. Cuidados antes de apresentar

- Existe uma **pasta duplicada antiga** em `C:\Users\joaog\OneDrive\Documentos\apex sem git`.
  Não apresente por ela.
- Existe um **projeto Android antigo (template vazio)** em
  `C:\Users\joaog\AndroidStudioProjects\ApexBebidas`. O correto é
  `E:\Programacao\Faculdade\2026\ApexBebidas`.
- Confirme o endereço do servidor no app na máquina da apresentação.
- Troque a chave `FISCAL-2026`.

---

## 12. Estrutura

```
src/main/java/br/edu/unifaj/cc/poo/apex/bebidas/
├── alerta/ analise/ aviso/ bebida/ denuncia/ distribuidor/ garrafa/
├── lote/ pessoa/ recall/ relatorio/ seguranca/ validacao/
├── CargaDadosInicial.java   # cenário de demonstração
└── Retorno.java             # envelope de resposta

src/main/resources/static/   # painel web + portal do consumidor
```
