/**
 * Chave de escrita da fiscalização.
 *
 * A chave NÃO fica no repositório: é pedida uma vez por sessão do navegador e
 * guardada em sessionStorage. Ao fechar a aba, desaparece.
 *
 * Uso nas páginas administrativas:
 *   fetchProtegido("/api/bebidas/fabricante", { method: "POST", ... })
 */
function chaveEscrita() {
    let chave = sessionStorage.getItem("chaveEscrita");
    if (!chave) {
        chave = prompt("Chave de escrita (fiscalização):");
        if (!chave) {
            return "";
        }
        chave = chave.trim();
        sessionStorage.setItem("chaveEscrita", chave);
    }
    return chave;
}

/**
 * Igual ao fetch, mas envia o cabeçalho X-Chave-Escrita.
 * Se o servidor recusar a chave, ela é apagada da sessão para ser pedida de
 * novo na próxima tentativa (evita ficar preso a uma chave errada).
 */
function fetchProtegido(url, opcoes) {
    const configuracao = Object.assign({}, opcoes || {});
    configuracao.headers = Object.assign({}, configuracao.headers || {}, {
        "X-Chave-Escrita": chaveEscrita()
    });

    return fetch(url, configuracao).then(function (resposta) {
        return resposta.clone().text().then(function (texto) {
            if (texto.indexOf("Chave de escrita") >= 0) {
                sessionStorage.removeItem("chaveEscrita");
            }
            return resposta;
        });
    });
}
