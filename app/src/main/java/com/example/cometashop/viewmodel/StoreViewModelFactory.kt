package com.example.cometashop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cometashop.data.repository.CategoriaRepository
import com.example.cometashop.data.repository.ProdutoRepository

class StoreViewModelFactory(
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
            return StoreViewModel(produtoRepository, categoriaRepository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}
