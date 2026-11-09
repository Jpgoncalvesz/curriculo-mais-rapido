# Implantar na Vercel

O repositório contém configurações para dois diretórios de projeto. A recomendação é importar a raiz do repositório. O site funciona sem API e faz a leitura dos currículos no navegador.

## Opção recomendada: raiz do repositório

Importe `Jpgoncalvesz/curriculo-mais-rapido` na Vercel e deixe **Root Directory** vazio ou como `.`. O `vercel.json` na raiz define:

| Configuração | Valor |
| --- | --- |
| Framework Preset | Other |
| Install Command | `npm --prefix frontend ci` |
| Build Command | `npm --prefix frontend run build` |
| Output Directory | `dist` |
| Node.js | 22.x |

A compilação é executada em `frontend`, mas o Vite grava o resultado em `dist` na raiz do repositório. O arquivo de configuração informa à Vercel onde instalar, compilar e encontrar o site.

## Alternativa: Root Directory = frontend

Se o projeto da Vercel já usa **Root Directory = frontend**, o arquivo `frontend/vercel.json` configura esse cenário:

| Configuração | Valor |
| --- | --- |
| Framework Preset | Vite |
| Install Command | `npm ci` |
| Build Command | `npm run build -- --outDir dist` |
| Output Directory | `dist` |
| Node.js | 22.x |

O argumento `--outDir dist` mantém a saída dentro de `frontend`, sem depender de arquivos fora do diretório selecionado. Não é necessário alterar a configuração do Vite usada pelos demais ambientes.

## Corrigir um projeto já importado

1. Abra **Settings → Build and Deployment** e confira o **Root Directory**.
2. Use um dos dois diretórios documentados acima.
3. Deixe os comandos e a saída serem definidos pelo `vercel.json`. Remova configurações antigas conflitantes, se houver.
4. Abra **Deployments** e acompanhe a nova implantação do commit enviado ao GitHub. Se uma implantação anterior continuar selecionada, reimplante a versão mais recente da branch `main`.
5. Não configure `VITE_API_URL` para a versão que processa currículos no navegador. Se houver um valor antigo como `/api` ou `localhost`, remova-o e reimplante.

O erro “No Output Directory named dist found” indica que a compilação e a saída esperada não estão alinhadas. Usar o `vercel.json` correspondente ao Root Directory resolve esse alinhamento.

## API Java opcional

Esta configuração publica o site React estático. A API Spring Boot precisa de um host Java ou contêiner separado; ela não é iniciada pelo build da Vercel.

Se desejar usar a API, publique-a em HTTPS e configure `VITE_API_URL` com sua URL completa terminando em `/api`, por exemplo `https://sua-api.example/api`. Na API, configure `ALLOWED_ORIGIN` com a origem exata do site publicado. Depois reimplante o front-end, pois as variáveis `VITE_` são incorporadas durante a compilação. Nunca coloque credenciais em variáveis `VITE_`.

## Validação local dos dois modos

Na raiz:

```sh
npm --prefix frontend ci
npm --prefix frontend run build
```

Confirme a existência de `dist/index.html`.

Com Root Directory = frontend:

```sh
cd frontend
npm run build -- --outDir dist
```

Confirme a existência de `frontend/dist/index.html` a partir da raiz do repositório.

Referências: [configuração de builds](https://vercel.com/docs/builds/configure-a-build) e [vercel.json](https://vercel.com/docs/project-configuration/vercel-json).
