# CometaShop — Trabalho 3 (Persistência com Room)

App Android em Kotlin + Jetpack Compose com loja com vários jogos (cada categoria é um jogo, com imagem). Categorias e produtos são salvos de verdade no banco local (Room/SQLite) e continuam lá depois de fechar o app.

## Como rodar

1. Clonar o repositório e abrir a pasta no Android Studio.
2. Aguardar o Gradle terminar o primeiro Sync (ou clicar em Sync Project with Gradle Files).
3. Escolher um emulador ou celular com Android 7.0 (API 24) ou superior.
4. Clicar em Run.

Para testar a persistência: criar uma categoria, criar um item do arsenal com foto, fechar o app por completo (remover dos recentes) e abrir de novo. Os dados devem continuar na lista.

## O que cada tela faz

- Home: tela dividida ao meio, com o lado CLIENTE (esquerda), o lado ADM (direita) e a logo no centro, que leva para Sobre nós.
- Loja (lado cliente): barra de pesquisa por nome de item, ícone de carrinho que abre uma barra lateral com os itens escolhidos, o valor de cada um, uma lixeira para remover e o total, carrossel com 5 itens destacados sorteados a cada vez que o app abre (3 aparecem por vez e trocam a cada 5 segundos) e os cards das categorias, 2 por linha.
- Itens da categoria: ao clicar em uma categoria, lista os itens dela (2 por linha), com pesquisa só dentro da categoria e um filtro de faixa de preço (ícone de cifrão).
- Menu do administrador: acesso ao gerenciamento de categorias e de itens.
- Categorias (ADM): criar, editar (ícone de lápis) e remover categorias, escolhendo a imagem da categoria na galeria.
- Itens (ADM): listar, criar, editar (ícone de lápis) e remover itens.
- Detalhes: busca o item no banco pelo id recebido na navegação e tem o botão para adicionar o item ao carrinho. Todos os preços aparecem em reais.
- Sobre nós: apresentação da equipe.

## Estrutura do projeto (MVVM)

| Pacote | Conteúdo |
|---|---|
| `data` | `CometaModels.kt` (Entities `Categoria`, com imagem opcional, e `Produto`, com `id: Int` em `@PrimaryKey(autoGenerate = true)` e chave estrangeira com `CASCADE`), `CategoriaDao.kt`, `ProdutoDao.kt` (o `buscarTodos()` devolve `Flow<List<...>>`), `AppDatabase.kt` (versão 2, com migração que adiciona a imagem da categoria; Singleton com `@Volatile` e `synchronized`) e `ImageStorage.kt` (copia a foto da galeria para a pasta interna do app, porque o link devolvido pela galeria é temporário). |
| `data.repository` | `CategoriaRepository` e `ProdutoRepository`: únicos que conversam com os DAOs. Os dois também cuidam de copiar e apagar as imagens. |
| `viewmodel` | `CategoryViewModel`, `ProductViewModel` e `StoreViewModel` (loja do cliente: destaques sorteados, pesquisa e faixa de preço) e `CartViewModel` (carrinho, que fica só na memória e não tem pagamento), cada um com seu `UiState` e sua Factory. |
| `ui` | Telas em Compose (`HomeScreeen.kt`, `StoreHomeScreen.kt`, `StoreCategoryScreen.kt`, `CarrinhoLateral.kt`, `CartoesLoja.kt`, `BarraLoja.kt`, `AdminMenuScreen.kt`, `CategoryScreen.kt`, `ProductScreen.kt`, `ProductDetailScreen.kt`, `AboutScreen.kt`) e o tema. As telas só leem o `uiState` e chamam métodos do ViewModel. |

Os dados chegam nas telas por `Flow`: quando a tabela muda, o Room atualiza a lista sozinho e a tela se redesenha, sem precisar buscar de novo.

## Dados iniciais

As categorias e itens que já vêm no app ficam na lista `dadosIniciais`, no arquivo `data/DadosIniciais.kt`. Eles são inseridos uma única vez, quando o banco é criado (primeira instalação do app). As imagens deles ficam em `app/src/main/res/drawable`, e a lista usa o nome do arquivo sem a extensão. Para testar de novo, desinstalar o app (ou limpar os dados dele) e rodar outra vez.

## Documentação do processo

Preencher com as palavras do trio e adicionar prints e vídeos. O link do Google Slides pode ficar aqui: ____________

### Como estava o Trabalho 2 e o que mudou
Os dados ficavam só em memória, dentro dos ViewModels. Agora ficam no Room. (complementar com a experiência do grupo)

### Entities, DAOs e Database
- `Categoria`: `id` e `nome`. Só o necessário para agrupar os itens.
- `Produto`: `id`, `categoriaId`, `nome`, `descricao`, `preco` e `imagemUri`. São os campos que a tela de arsenal já mostrava e filtrava.
- Os dois DAOs têm os quatro métodos básicos. Extras: `buscarPorId` (usado na tela de detalhes) e `buscarPorCategoria` (usado para apagar as fotos dos produtos quando uma categoria é removida).

### Dificuldades na migração
(preencher: o que quebrou e como resolveram)

### Teste de persistência
(inserir print ou vídeo: adicionar item, fechar o app, reabrir e o item continua lá)

### Dificuldades de cada integrante
(preencher)

## Trabalho 4 — Arquitetura MVVM

### Como estava o projeto no Trabalho 3 e o que mudou na reorganização
(preencher com as palavras do trio)

### Como o trio dividiu os pacotes
(preencher com as palavras do trio)

### Por que o Repository existe
(preencher com as palavras do trio)

### O que tem dentro do UiState de cada tela
(preencher com as palavras do trio)

### Alguma funcionalidade quebrou durante a reorganização?
(preencher: o que quebrou e como encontraram e corrigiram)

### Dificuldades de cada integrante
(preencher)
