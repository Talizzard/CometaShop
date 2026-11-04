package com.example.cometashop.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cometashop.data.Categoria
import com.example.cometashop.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "CategoryViewModel"

class CategoryViewModel(private val repository: CategoriaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        observarCategorias()
    }

    private fun observarCategorias() {
        viewModelScope.launch {
            repository.buscarTodos()
                .catch { e ->
                    Log.e(TAG, "Erro ao buscar categorias", e)
                    _uiState.update { it.copy(carregando = false) }
                }
                .collect { lista ->
                    _uiState.update { it.copy(categorias = lista, carregando = false) }
                }
        }
    }

    fun aoMudarNomeNovaCategoria(texto: String) {
        _uiState.update { it.copy(nomeNovaCategoria = texto) }
    }

    fun aoEscolherImagemNova(uri: String) {
        _uiState.update { it.copy(imagemNova = uri) }
    }

    fun adicionarCategoria() {
        val nome = _uiState.value.nomeNovaCategoria
        val imagem = _uiState.value.imagemNova
        _uiState.update { it.copy(nomeNovaCategoria = "", imagemNova = null) }
        if (nome.isBlank()) return
        viewModelScope.launch {
            try {
                repository.inserir(Categoria(nome = nome.trim(), imagemUri = imagem))
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao inserir categoria", e)
            }
        }
    }

    fun iniciarEdicao(categoria: Categoria) {
        _uiState.update {
            it.copy(
                categoriaEmEdicao = categoria,
                nomeEditado = categoria.nome,
                imagemEditada = categoria.imagemUri
            )
        }
    }

    fun aoMudarNomeEditado(texto: String) {
        _uiState.update { it.copy(nomeEditado = texto) }
    }

    fun aoEscolherImagemEditada(uri: String) {
        _uiState.update { it.copy(imagemEditada = uri) }
    }

    fun cancelarEdicao() {
        _uiState.update {
            it.copy(categoriaEmEdicao = null, nomeEditado = "", imagemEditada = null)
        }
    }

    fun salvarEdicao() {
        val categoria = _uiState.value.categoriaEmEdicao ?: return
        val novoNome = _uiState.value.nomeEditado
        val novaImagem = _uiState.value.imagemEditada
        cancelarEdicao()
        if (novoNome.isBlank()) return
        viewModelScope.launch {
            try {
                repository.atualizar(categoria.copy(nome = novoNome.trim(), imagemUri = novaImagem))
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao atualizar categoria", e)
            }
        }
    }

    fun removerCategoria(categoria: Categoria) {
        viewModelScope.launch {
            try {
                repository.deletar(categoria)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao deletar categoria", e)
            }
        }
    }
}
