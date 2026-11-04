package com.example.cometashop.data.repository

import android.content.Context
import com.example.cometashop.data.ImageStorage
import com.example.cometashop.data.Produto
import com.example.cometashop.data.ProdutoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProdutoRepository(
    private val produtoDao: ProdutoDao,
    private val context: Context
) {

    fun buscarTodos(): Flow<List<Produto>> = produtoDao.buscarTodos()

    suspend fun buscarPorId(id: Int): Produto? = produtoDao.buscarPorId(id)

    suspend fun buscarPorCategoria(categoriaId: Int): List<Produto> =
        produtoDao.buscarPorCategoria(categoriaId)

    suspend fun inserir(produto: Produto) {
        val imagemSalva = withContext(Dispatchers.IO) {
            ImageStorage.copiar(context, produto.imagemUri)
        }
        produtoDao.inserir(produto.copy(imagemUri = imagemSalva))
    }

    suspend fun atualizar(produto: Produto) {
        val atual = produtoDao.buscarPorId(produto.id) ?: return
        val imagemFinal = if (produto.imagemUri != atual.imagemUri) {
            withContext(Dispatchers.IO) {
                val nova = ImageStorage.copiar(context, produto.imagemUri)
                ImageStorage.apagar(context, atual.imagemUri)
                nova
            }
        } else {
            atual.imagemUri
        }
        produtoDao.atualizar(produto.copy(imagemUri = imagemFinal))
    }

    suspend fun deletar(produto: Produto) {
        produtoDao.deletar(produto)
        withContext(Dispatchers.IO) {
            ImageStorage.apagar(context, produto.imagemUri)
        }
    }
}
