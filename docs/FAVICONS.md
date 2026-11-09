# Opções de favicon

Dois desenhos vetoriais originais, alinhados à identidade verde do projeto:

1. `frontend/public/favicon-curriculo.svg`: documento com sinal de mais. Mantém continuidade com a marca do site.
2. `frontend/public/favicon-movimento.svg`: monograma CM com seta de avanço. Destaca as iniciais e a ideia de progresso.

O proprietário escolheu a opção **CM em movimento**. O monograma foi refinado com letras mais pesadas e inclinadas, desenhadas a partir de Arial Black e convertidas em contornos vetoriais. A seta permanece em verde-claro. Não há dependência de fontes instaladas no dispositivo do visitante.

O head de `frontend/index.html` já utiliza a opção escolhida:

```html
<link rel="icon" type="image/svg+xml" href="/favicon-movimento.svg?v=2">
<link rel="icon" type="image/x-icon" sizes="16x16 32x32 48x48" href="/favicon.ico?v=2">
<link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png?v=2">
```

O SVG é a versão principal. O ICO contém versões de 16, 32 e 48 pixels, e o PNG de 180 pixels atende atalhos Apple. O parâmetro de versão atualiza o endereço do recurso para navegadores que mantêm o favicon em cache. A opção de documento permanece disponível como alternativa no código.
