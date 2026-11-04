package com.example.cometashop.viewmodel

import com.example.cometashop.data.Produto

data class ProductUiState(
    val produtos: List<Produto> = emptyList(),
    val carregando: Boolean = true,
    val produtoSelecionado: Produto? = null,
    val detalheCarregado: Boolean = false,
    val dialogoAberto: Boolean = false,
    val produtoEmEdicaoId: Int? = null,
    val formNome: String = "",
    val formDescricao: String = "",
    val formPreco: String = "",
    val formCategoriaId: Int? = null,
    val formImagemUri: String? = null
)
