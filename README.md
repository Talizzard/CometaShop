# CometaShop — Trabalho 3 (Persistência com Room)

App Android em Kotlin + Jetpack Compose com catálogo de armas de The Finals. Categorias e produtos são salvos de verdade no banco local (Room/SQLite) e continuam lá depois de fechar o app.

## Como rodar

1. Clonar o repositório e abrir a pasta no Android Studio.
2. Aguardar o Gradle terminar o primeiro Sync (ou clicar em Sync Project with Gradle Files).
3. Escolher um emulador ou celular com Android 7.0 (API 24) ou superior.
4. Clicar em Run.

Para testar a persistência: criar uma categoria, criar um item do arsenal com foto, fechar o app por completo (remover dos recentes) e abrir de novo. Os dados devem continuar na lista.

## O que cada tela faz

- Home: acesso às telas de categorias, arsenal e sobre nós.
- Categorias: criar, editar (ícone de lápis) e remover categorias.
- Arsenal: listar, filtrar, criar, editar (ícone de lápis) e remover itens.
- Detalhes: busca o item no banco pelo id recebido na navegação.
- Sobre nós: apresentação da equipe.

## Estrutura do Room

| Arquivo | Conteúdo |
|---|---|
| `model/CometaModels.kt` | Entities `Categoria` e `Produto`, ambas com `id: Int` em `@PrimaryKey(autoGenerate = true)`. `Produto` tem chave estrangeira para `Categoria` com `CASCADE`. |
| `model/CategoriaDao.kt` | `inserir`, `buscarTodos`, `atualizar`, `deletar` e `buscarPorId`, todos `suspend`. |
| `model/ProdutoDao.kt` | `inserir`, `buscarTodos`, `atualizar`, `deletar`, `buscarPorId` e `buscarPorCategoria`, todos `suspend`. |
| `model/AppDatabase.kt` | `@Database` com as duas entities, versão 1, Singleton com `companion object`, `@Volatile` e `synchronized`. |
| `model/ImageStorage.kt` | Copia a foto escolhida na galeria para a pasta interna do app, porque o link devolvido pela galeria é temporário. |
| `viewmodel/CategoryViewModel.kt` | Operações de banco das categorias em `CoroutineScope(Dispatchers.IO).launch`, com `try/catch` e `Log.e`. |
| `viewmodel/ProductViewModel.kt` | Mesmo padrão para os produtos, incluindo a busca por id da tela de detalhes. |

As telas buscam os dados com `LaunchedEffect` assim que abrem.

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
