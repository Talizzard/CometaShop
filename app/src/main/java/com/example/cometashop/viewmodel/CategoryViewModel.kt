package com.example.cometashop.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.cometashop.model.AppDatabase
import com.example.cometashop.model.Categoria
import com.example.cometashop.model.ImageStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "CategoryViewModel"

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.obterInstancia(application)
    private val categoriaDao = db.categoriaDao()
    private val produtoDao = db.produtoDao()

    private val _categories = MutableStateFlow<List<Categoria>>(emptyList())
    val categories: StateFlow<List<Categoria>> = _categories.asStateFlow()

    fun carregar() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _categories.value = categoriaDao.buscarTodos()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao buscar categorias", e)
            }
        }
    }

    fun addCategory(nome: String) {
        if (nome.isBlank()) return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                categoriaDao.inserir(Categoria(nome = nome.trim()))
                _categories.value = categoriaDao.buscarTodos()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao inserir categoria", e)
            }
        }
    }

    fun updateCategory(categoria: Categoria, novoNome: String) {
        if (novoNome.isBlank()) return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                categoriaDao.atualizar(categoria.copy(nome = novoNome.trim()))
                _categories.value = categoriaDao.buscarTodos()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao atualizar categoria", e)
            }
        }
    }

    fun removeCategory(categoria: Categoria) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val produtos = produtoDao.buscarPorCategoria(categoria.id)
                categoriaDao.deletar(categoria)
                produtos.forEach { ImageStorage.apagar(getApplication<Application>(), it.imagemUri) }
                _categories.value = categoriaDao.buscarTodos()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao deletar categoria", e)
            }
        }
    }
}
