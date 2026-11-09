# Como contribuir

1. Abra uma issue descrevendo o problema ou a sugestão. Não anexe currículos reais nem dados pessoais.
2. Faça um fork, crie uma branch e mantenha a mudança focada.
3. Use Node.js 22.12+ e JDK 21 com Maven 3.9+.
4. Execute `npm ci`, `npm test` e `npm run build` na pasta frontend.
5. Execute `mvn -f backend/pom.xml verify` e `node --test extension/scan.test.cjs extension/fill.test.cjs` na raiz.
6. Descreva no pull request o problema, o resultado e os testes realizados.

Use somente dados fictícios nos testes. Não adicione tokens, arquivos de perfil, documentos de candidatos, dependências instaladas ou resultados de compilação ao repositório.
