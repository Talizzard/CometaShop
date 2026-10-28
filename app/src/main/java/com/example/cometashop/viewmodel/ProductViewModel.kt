package com.example.cometashop.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.cometashop.model.AppDatabase
import com.example.cometashop.model.ImageStorage
import com.example.cometashop.model.Produto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "ProductViewModel"

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val produtoDao = AppDatabase.obterInstancia(application).produtoDao()

    private val _products = MutableStateFlow<List<Produto>>(emptyList())
    val products: StateFlow<List<Produto>> = _products.asStateFlow()

    private val _produtoSelecionado = MutableStateFlow<Produto?>(null)
    val produtoSelecionado: StateFlow<Produto?> = _produtoSelecionado.asStateFlow()

    private val _detalheCarregado = MutableStateFlow(false)
    val detalheCarregado: StateFlow<Boolean> = _detalheCarregado.asStateFlow()

    fun carregar() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _products.value = produtoDao.buscarTodos()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao buscar produtos", e)
            }
        }
    }

    fun carregarProduto(id: Int) {
        _produtoSelecionado.value = null
        _detalheCarregado.value = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _produtoSelecionado.value = produtoDao.buscarPorId(id)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao buscar produto por id", e)
            } finally {
                _detalheCarregado.value = true
            }
        }
    }

    fun addProduct(
        nome: String,
        categoriaId: Int,
        descricao: String,
        preco: Double,
        imagemUri: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imagemSalva = ImageStorage.copiar(getApplication<Application>(), imagemUri)
                produtoDao.inserir(
                    Produto(
                        categoriaId = categoriaId,
                        nome = nome.trim(),
                        descricao = descricao.trim(),
                        preco = preco,
                        imagemUri = imagemSalva
                    )
                )
                _products.value = produtoDao.buscarTodos()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao inserir produto", e)
            }
        }
    }

    fun updateProduct(
        id: Int,
        nome: String,
        categoriaId: Int,
        descricao: String,
        preco: Double,
        imagemUri: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val atual = produtoDao.buscarPorId(id) ?: return@launch
                val imagemFinal = if (imagemUri != atual.imagemUri) {
                    val nova = ImageStorage.copiar(getApplication<Application>(), imagemUri)
                    ImageStorage.apagar(getApplication<Application>(), atual.imagemUri)
                    nova
                } else {
                    atual.imagemUri
                }
                produtoDao.atualizar(
                    atual.copy(
                        categoriaId = categoriaId,
                        nome = nome.trim(),
                        descricao = descricao.trim(),
                        preco = preco,
                        imagemUri = imagemFinal
                    )
                )
                _products.value = produtoDao.buscarTodos()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao atualizar produto", e)
            }
        }
    }

    fun removeProduct(produto: Produto) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                produtoDao.deletar(produto)
                ImageStorage.apagar(getApplication<Application>(), produto.imagemUri)
                _products.value = produtoDao.buscarTodos()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao deletar produto", e)
            }
        }
    }
}
