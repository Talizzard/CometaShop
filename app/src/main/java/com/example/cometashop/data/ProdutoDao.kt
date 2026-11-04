package com.example.cometashop.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert
    suspend fun inserir(produto: Produto)

    @Query("SELECT * FROM produtos ORDER BY nome")
    fun buscarTodos(): Flow<List<Produto>>

    @Update
    suspend fun atualizar(produto: Produto)

    @Delete
    suspend fun deletar(produto: Produto)

    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Produto?

    @Query("SELECT * FROM produtos WHERE categoriaId = :categoriaId")
    suspend fun buscarPorCategoria(categoriaId: Int): List<Produto>
}
