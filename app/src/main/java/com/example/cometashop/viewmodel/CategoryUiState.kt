package com.example.cometashop.viewmodel

import com.example.cometashop.data.Categoria

data class CategoryUiState(
    val categorias: List<Categoria> = emptyList(),
    val carregando: Boolean = true,
    val nomeNovaCategoria: String = "",
    val imagemNova: String? = null,
    val categoriaEmEdicao: Categoria? = null,
    val nomeEditado: String = "",
    val imagemEditada: String? = null
)
