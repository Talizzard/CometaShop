package com.example.cometashop.viewmodel

import com.example.cometashop.data.Produto

data class ItemCarrinho(
    val idLinha: Long,
    val produto: Produto
)

data class CartUiState(
    val itens: List<ItemCarrinho> = emptyList()
) {
    val total: Double
        get() = itens.sumOf { it.produto.preco }
}
