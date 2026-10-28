package com.example.cometashop.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String
)

@Entity(
    tableName = "produtos",
    foreignKeys = [
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoriaId")]
)
data class Produto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoriaId: Int,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val imagemUri: String
)
