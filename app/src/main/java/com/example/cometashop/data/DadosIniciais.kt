package com.example.cometashop.data

data class ItemInicial(
    val nome: String,
    val descricao: String,
    val preco: Double,
    val imagem: String
)

data class CategoriaInicial(
    val nome: String,
    val imagem: String,
    val itens: List<ItemInicial>
)

val dadosIniciais = listOf(
    CategoriaInicial(
            nome = "Valorant",
            imagem = "valorant",
            itens = listOf(
                ItemInicial(
                    nome = "Vandal",
                    descricao = "Rifle de assalto de alto dano.",
                    preco = 2900.0,
                    imagem = "v1"
                ),
                ItemInicial(
                    nome = "Phantom Protocolo",
                    descricao = "Rifle de assalto com tiro silencioso.",
                    preco = 2900.0,
                    imagem = "v2"
                ),
                ItemInicial(
                    nome = "Vandal Kuronami",
                    descricao = "Skin temática com efeitos especiais de água e acabamento impecável.",
                    preco = 3550.0,
                    imagem = "v3"
                ),
                ItemInicial(
                    nome = "Vandal Saqueadora",
                    descricao = "Uma das skins mais populares com animação de abate sombria.",
                    preco = 2175.0,
                    imagem = "v4"
                ),
                ItemInicial(
                    nome = "Phantom Ion",
                    descricao = "Visual futurista com efeitos de energia azul brilhante.",
                    preco = 1775.0,
                    imagem = "v5"
                )
            )
        ),
        CategoriaInicial(
            nome = "The Finals",
            imagem = "finals",
            itens = listOf(
                ItemInicial(
                    nome = "AKM",
                    descricao = "Rifle de assalto robusto para combate médio.",
                    preco = 1500.0,
                    imagem = "t1"
                ),
                ItemInicial(
                    nome = "FCAR",
                    descricao = "Rifle de precisão semiautomático.",
                    preco = 1800.0,
                    imagem = "t2"
                ),
                ItemInicial(
                    nome = "AKM Dourada",
                    descricao = "Versão clássica revestida inteiramente em ouro brilhante.",
                    preco = 850.0,
                    imagem = "t3"
                ),
                ItemInicial(
                    nome = "FCAR Neon Cyber",
                    descricao = "Estampa urbana com luzes neon vibrantes para combates noturnos.",
                    preco = 2400.0,
                    imagem = "t4"
                ),
                ItemInicial(
                    nome = "LH1 Brilho Estelar",
                    descricao = "Acabamento metálico cromado que reflete as luzes da arena.",
                    preco = 1250.0,
                    imagem = "t5"
                )
            )
        ),
        CategoriaInicial(
            nome = "League of Legends",
            imagem = "lol",
            itens = listOf(
                ItemInicial(
                    nome = "Yasuo Emissário da Escuridão",
                    descricao = "Skin lendária com temática demoníaca e animações fluidas.",
                    preco = 1820.0,
                    imagem = "l1"
                ),
                ItemInicial(
                    nome = "Lux Elementalista",
                    descricao = "Permite alternar entre 10 formas elementais diferentes durante a partida.",
                    preco = 3250.0,
                    imagem = "l2"
                ),
                ItemInicial(
                    nome = "Ahri Florescer Espiritual",
                    descricao = "Visual místico inspirado em festivais de espíritos e folclore asiático.",
                    preco = 1350.0,
                    imagem = "l3"
                ),
                ItemInicial(
                    nome = "Zed Predador das Ruas",
                    descricao = "Visual moderno com temática urbana e efeitos visuais sombrios.",
                    preco = 1350.0,
                    imagem = "l4"
                ),
                ItemInicial(
                    nome = "Jinx Guardiã Estelar",
                    descricao = "Skin colorida com temática de anime e efeitos mágicos.",
                    preco = 1820.0,
                    imagem = "l5"
                )
            )
        ),
        CategoriaInicial(
            nome = "Overwatch 2",
            imagem = "ow",
            itens = listOf(
                ItemInicial(
                    nome = "Genji Mão de Midas",
                    descricao = "Visual luxuoso com detalhes dourados reluzentes.",
                    preco = 1900.0,
                    imagem = "o1"
                ),
                ItemInicial(
                    nome = "Mercy Bruxa",
                    descricao = "Uma das skins mais queridas da comunidade para o Halloween.",
                    preco = 1000.0,
                    imagem = "o2"
                ),
                ItemInicial(
                    nome = "Reinhardt Imperador Demônio",
                    descricao = "Armadura pesada intimidadora com chifres e detalhes de lava.",
                    preco = 3000.0,
                    imagem = "o3"
                ),
                ItemInicial(
                    nome = "Tracer Ciberciclo",
                    descricao = "Visual futurista com capacete fechado e detalhes em neon.",
                    preco = 1500.0,
                    imagem = "o4"
                ),
                ItemInicial(
                    nome = "Kiriko Guaxinim",
                    descricao = "Roupa casual estilosa combinada com acessórios temáticos.",
                    preco = 2200.0,
                    imagem = "o5"
                )
            )
        ),
        CategoriaInicial(
            nome = "Fortnite",
            imagem = "fot",
            itens = listOf(
                ItemInicial(
                    nome = "Cavaleiro Negro",
                    descricao = "Skin clássica e intimidadora das primeiras temporadas do Passe de Batalha.",
                    preco = 3800.0,
                    imagem = "f1"
                ),
                ItemInicial(
                    nome = "Aura",
                    descricao = "Traje minimalista e ágil muito utilizado por jogadores competitivos.",
                    preco = 800.0,
                    imagem = "f2"
                ),
                ItemInicial(
                    nome = "Midas",
                    descricao = "Personagem que transforma qualquer arma equipada em ouro puro.",
                    preco = 2000.0,
                    imagem = "f3"
                ),
                ItemInicial(
                    nome = "Harley Quinn",
                    descricao = "Visual oficial inspirado no universo cinematográfico da personagem.",
                    preco = 1500.0,
                    imagem = "f4"
                ),
                ItemInicial(
                    nome = "Goku",
                    descricao = "Traje do famoso herói de anime com estilos alternativos de transformação.",
                    preco = 2500.0,
                    imagem = "f5"
                )
            )
        )
    )
