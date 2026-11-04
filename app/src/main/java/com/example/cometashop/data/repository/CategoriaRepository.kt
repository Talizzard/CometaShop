package com.example.cometashop.data.repository

import android.content.Context
import com.example.cometashop.data.Categoria
import com.example.cometashop.data.CategoriaDao
import com.example.cometashop.data.ImageStorage
import com.example.cometashop.data.ProdutoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CategoriaRepository(
    private val categoriaDao: CategoriaDao,
    private val produtoDao: ProdutoDao,
    private val context: Context
) {

    fun buscarTodos(): Flow<List<Categoria>> = categoriaDao.buscarTodos()

    suspend fun buscarPorId(id: Int): Categoria? = categoriaDao.buscarPorId(id)

    suspend fun inserir(categoria: Categoria) {
        val imagemSalva = categoria.imagemUri?.let { uri ->
            withContext(Dispatchers.IO) { ImageStorage.copiar(context, uri) }
        }
        categoriaDao.inserir(categoria.copy(imagemUri = imagemSalva))
    }

    suspend fun atualizar(categoria: Categoria) {
        val atual = categoriaDao.buscarPorId(categoria.id) ?: return
        val imagemFinal = if (categoria.imagemUri != atual.imagemUri) {
            withContext(Dispatchers.IO) {
                val nova = categoria.imagemUri?.let { ImageStorage.copiar(context, it) }
                atual.imagemUri?.let { ImageStorage.apagar(context, it) }
                nova
            }
        } else {
            atual.imagemUri
        }
        categoriaDao.atualizar(categoria.copy(imagemUri = imagemFinal))
    }

    suspend fun deletar(categoria: Categoria) {
        val produtos = produtoDao.buscarPorCategoria(categoria.id)
        categoriaDao.deletar(categoria)
        withContext(Dispatchers.IO) {
            produtos.forEach { ImageStorage.apagar(context, it.imagemUri) }
            categoria.imagemUri?.let { ImageStorage.apagar(context, it) }
        }
    }
}
