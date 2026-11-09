# Validação da versão 1.0

- Build de produção: TypeScript e Vite.
- 6 testes do perfil: reconhecimento de contatos e seções, títulos de documento, acentos, texto vazio e validação do e-mail.
- 13 testes Java: extração TXT, PDF e DOCX reais, rotas HTTP, upload ausente/vazio, limite de tamanho, conteúdo binário disfarçado, texto insuficiente e CORS.
- 7 testes da extensão: correspondência de contatos, confirmação de e-mail, separação de nomes, exclusão de campos protegidos e preenchimento com eventos. Verificam também que campos movidos, bloqueados ou alterados depois da revisão não são preenchidos.
- Verificação visual no navegador: tela inicial nos modos claro e escuro.
- Fluxo verificado na interface com dados fictícios: colar texto, reconhecer campos, revisar e salvar. Os dados e o tema permaneceram após recarregar.
- Auditoria npm: sem vulnerabilidades reportadas após atualização das dependências.

## Limites da verificação

Não foram testadas todas as plataformas de emprego. A extensão não foi publicada em loja e requer instalação manual. A API Java é distribuída com o código e o pacote executável; o endereço Sites utiliza a versão estática com leitura local. O fluxo de impressão depende da janela de impressão do navegador.

## Reproduzir

```sh
cd frontend
npm ci
npm test
npm run build
npm audit
```

```sh
mvn -f backend/pom.xml verify
node --test extension/scan.test.cjs extension/fill.test.cjs
```
