package com.example.cometashop.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoriaDao {
    @Insert
    suspend fun inserir(categoria: Categoria)

    @Query("SELECT * FROM categorias ORDER BY nome")
    suspend fun buscarTodos(): List<Categoria>

    @Update
    suspend fun atualizar(categoria: Categoria)

    @Delete
    suspend fun deletar(categoria: Categoria)

    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun buscarPorId(id: Int): Categoria?
}
