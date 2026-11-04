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
        imagem = "cat_valorant",
        itens = listOf(
            ItemInicial(
                nome = "Vandal",
                descricao = "Rifle de assalto de alto dano.",
                preco = 2900.0,
                imagem = "item_vandal"
            ),
            ItemInicial(
                nome = "Phantom",
                descricao = "Rifle de assalto com tiro silencioso.",
                preco = 2900.0,
                imagem = "item_phantom"
            )
        )
    ),
    CategoriaInicial(
        nome = "The Finals",
        imagem = "cat_thefinals",
        itens = listOf(
            ItemInicial(
                nome = "AKM",
                descricao = "Rifle de assalto robusto para combate médio.",
                preco = 1500.0,
                imagem = "item_akm"
            ),
            ItemInicial(
                nome = "FCAR",
                descricao = "Rifle de precisão semiautomático.",
                preco = 1800.0,
                imagem = "item_fcar"
            )
        )
    )
)
