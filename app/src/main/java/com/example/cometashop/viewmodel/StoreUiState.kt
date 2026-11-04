package com.example.cometashop.viewmodel

import com.example.cometashop.data.Categoria
import com.example.cometashop.data.Produto
import kotlin.math.ceil

data class StoreUiState(
    val categorias: List<Categoria> = emptyList(),
    val produtos: List<Produto> = emptyList(),
    val carregando: Boolean = true,
    val destaquesIds: List<Int> = emptyList(),
    val buscaLoja: String = "",
    val categoriaSelecionadaId: Int? = null,
    val buscaCategoria: String = "",
    val faixaPreco: ClosedFloatingPointRange<Float>? = null
) {
    val destaques: List<Produto>
        get() = destaquesIds.mapNotNull { id -> produtos.firstOrNull { it.id == id } }

    val buscandoNaLoja: Boolean
        get() = buscaLoja.isNotBlank()

    val resultadosDaLoja: List<Produto>
        get() = produtos.filter { it.nome.contains(buscaLoja.trim(), ignoreCase = true) }

    val produtosDaCategoria: List<Produto>
        get() = produtos.filter { it.categoriaId == categoriaSelecionadaId }

    val limitePreco: Float
        get() = ceil(produtosDaCategoria.maxOfOrNull { it.preco } ?: 0.0).toFloat().coerceAtLeast(10f)

    val faixaEfetiva: ClosedFloatingPointRange<Float>
        get() {
            val faixa = faixaPreco ?: return 0f..limitePreco
            return faixa.start.coerceIn(0f, limitePreco)..faixa.endInclusive.coerceIn(0f, limitePreco)
        }

    val itensDaCategoriaFiltrados: List<Produto>
        get() = produtosDaCategoria.filter { produto ->
            val combinaNome = produto.nome.contains(buscaCategoria.trim(), ignoreCase = true)
            val faixa = faixaPreco
            val combinaPreco = faixa == null ||
                (produto.preco >= faixa.start && produto.preco <= faixa.endInclusive)
            combinaNome && combinaPreco
        }
}
