package com.example.cometashop.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cometashop.data.Produto
import com.example.cometashop.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ProductViewModel"

class ProductViewModel(private val repository: ProdutoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        observarProdutos()
    }

    private fun observarProdutos() {
        viewModelScope.launch {
            repository.buscarTodos()
                .catch { e ->
                    Log.e(TAG, "Erro ao buscar produtos", e)
                    _uiState.update { it.copy(carregando = false) }
                }
                .collect { lista ->
                    _uiState.update { it.copy(produtos = lista, carregando = false) }
                }
        }
    }

    fun carregarProduto(id: Int) {
        _uiState.update { it.copy(produtoSelecionado = null, detalheCarregado = false) }
        viewModelScope.launch {
            try {
                val produto = repository.buscarPorId(id)
                _uiState.update { it.copy(produtoSelecionado = produto) }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao buscar produto por id", e)
            } finally {
                _uiState.update { it.copy(detalheCarregado = true) }
            }
        }
    }

    fun abrirDialogoNovo(categoriaPadraoId: Int?) {
        _uiState.update {
            it.copy(
                dialogoAberto = true,
                produtoEmEdicaoId = null,
                formNome = "",
                formDescricao = "",
                formPreco = "",
                formCategoriaId = categoriaPadraoId,
                formImagemUri = null
            )
        }
    }

    fun abrirDialogoEdicao(produto: Produto) {
        _uiState.update {
            it.copy(
                dialogoAberto = true,
                produtoEmEdicaoId = produto.id,
                formNome = produto.nome,
                formDescricao = produto.descricao,
                formPreco = produto.preco.toString(),
                formCategoriaId = produto.categoriaId,
                formImagemUri = produto.imagemUri
            )
        }
    }

    fun fecharDialogo() {
        _uiState.update { it.copy(dialogoAberto = false) }
    }

    fun aoMudarNome(texto: String) {
        _uiState.update { it.copy(formNome = texto) }
    }

    fun aoMudarDescricao(texto: String) {
        _uiState.update { it.copy(formDescricao = texto) }
    }

    fun aoMudarPreco(texto: String) {
        _uiState.update { it.copy(formPreco = texto) }
    }

    fun aoEscolherCategoria(categoriaId: Int) {
        _uiState.update { it.copy(formCategoriaId = categoriaId) }
    }

    fun aoEscolherImagem(uri: String) {
        _uiState.update { it.copy(formImagemUri = uri) }
    }

    fun salvarProduto() {
        val estado = _uiState.value
        val categoriaId = estado.formCategoriaId
        val imagemUri = estado.formImagemUri
        if (estado.formNome.isBlank() || categoriaId == null || imagemUri == null) return

        val preco = estado.formPreco.toDoubleOrNull() ?: 0.0
        val idEmEdicao = estado.produtoEmEdicaoId
        fecharDialogo()

        viewModelScope.launch {
            try {
                if (idEmEdicao == null) {
                    repository.inserir(
                        Produto(
                            categoriaId = categoriaId,
                            nome = estado.formNome.trim(),
                            descricao = estado.formDescricao.trim(),
                            preco = preco,
                            imagemUri = imagemUri
                        )
                    )
                } else {
                    repository.atualizar(
                        Produto(
                            id = idEmEdicao,
                            categoriaId = categoriaId,
                            nome = estado.formNome.trim(),
                            descricao = estado.formDescricao.trim(),
                            preco = preco,
                            imagemUri = imagemUri
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao salvar produto", e)
            }
        }
    }

    fun removerProduto(produto: Produto) {
        viewModelScope.launch {
            try {
                repository.deletar(produto)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao deletar produto", e)
            }
        }
    }
}
