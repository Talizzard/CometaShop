package com.example.cometashop.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cometashop.data.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private var proximaLinha = 0L

    fun adicionar(produto: Produto) {
        val linha = proximaLinha++
        _uiState.update { it.copy(itens = it.itens + ItemCarrinho(linha, produto)) }
    }

    fun remover(idLinha: Long) {
        _uiState.update { estado ->
            estado.copy(itens = estado.itens.filterNot { it.idLinha == idLinha })
        }
    }
}
