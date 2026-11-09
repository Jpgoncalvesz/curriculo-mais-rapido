# API Java

Base local: `http://127.0.0.1:8080/api`.

## GET /health

Resposta 200: `{ "status": "ok", "service": "curriculo-mais-rapido" }`.

## POST /resume/extract

Envie `multipart/form-data`, campo `file`, com PDF, DOCX ou TXT. Limite: 5 MB. A API inspeciona o conteúdo usando Tika, extrai até 200 mil caracteres e descarta o arquivo após a requisição.

```sh
curl -F "file=@curriculo.pdf" http://127.0.0.1:8080/api/resume/extract
```

Resposta 200: `{ "text": "conteúdo extraído...", "reviewRequired": true }`.

Erros: 400 para vazio ou tamanho inválido, 413 para limite de upload do servidor, 415 para conteúdo não aceito e 422 para documento ilegível ou sem texto suficiente. Não são retornadas pilhas de erro ou detalhes internos do parser.

## Configuração

| Variável | Padrão | Uso |
| --- | --- | --- |
| PORT | 8080 | Porta HTTP |
| SERVER_ADDRESS | 127.0.0.1 | Interface de escuta |
| ALLOWED_ORIGIN | http://127.0.0.1:5173 | Origem autorizada no CORS |
| VITE_API_URL | vazio | Front-end: vazio para leitura local; `/api` para Java via proxy |

CORS não é autenticação. A configuração padrão atende desenvolvimento local. Para servidor público, aplique os controles descritos no README e use HTTPS.
