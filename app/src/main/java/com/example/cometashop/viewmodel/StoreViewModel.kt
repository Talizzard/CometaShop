package com.example.cometashop.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cometashop.data.repository.CategoriaRepository
import com.example.cometashop.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "StoreViewModel"
private const val QUANTIDADE_DESTAQUES = 5

class StoreViewModel(
    private val produtoRepository: ProdutoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    init {
        observarCategorias()
        observarProdutos()
    }

    private fun observarCategorias() {
        viewModelScope.launch {
            categoriaRepository.buscarTodos()
                .catch { e -> Log.e(TAG, "Erro ao buscar categorias", e) }
                .collect { lista ->
                    _uiState.update { it.copy(categorias = lista) }
                }
        }
    }

    private fun observarProdutos() {
        viewModelScope.launch {
            produtoRepository.buscarTodos()
                .catch { e ->
                    Log.e(TAG, "Erro ao buscar produtos", e)
                    _uiState.update { it.copy(carregando = false) }
                }
                .collect { lista ->
                    _uiState.update { estado ->
                        val presentes = estado.destaquesIds.count { id -> lista.any { it.id == id } }
                        val desejados = minOf(QUANTIDADE_DESTAQUES, lista.size)
                        val novosIds = if (presentes < desejados) {
                            lista.shuffled().take(QUANTIDADE_DESTAQUES).map { it.id }
                        } else {
                            estado.destaquesIds
                        }
                        estado.copy(produtos = lista, carregando = false, destaquesIds = novosIds)
                    }
                }
        }
    }

    fun aoMudarBuscaLoja(texto: String) {
        _uiState.update { it.copy(buscaLoja = texto) }
    }

    fun abrirCategoria(categoriaId: Int) {
        _uiState.update {
            it.copy(categoriaSelecionadaId = categoriaId, buscaCategoria = "", faixaPreco = null)
        }
    }

    fun aoMudarBuscaCategoria(texto: String) {
        _uiState.update { it.copy(buscaCategoria = texto) }
    }

    fun aoMudarFaixaPreco(faixa: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(faixaPreco = faixa) }
    }

    fun limparFaixaPreco() {
        _uiState.update { it.copy(faixaPreco = null) }
    }
}
