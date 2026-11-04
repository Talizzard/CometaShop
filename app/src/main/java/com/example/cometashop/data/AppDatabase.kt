package com.example.cometashop.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRACAO_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE categorias ADD COLUMN imagemUri TEXT")
    }
}

private fun uriDaImagem(context: Context, nome: String): String? {
    val id = context.resources.getIdentifier(nome, "drawable", context.packageName)
    return if (id == 0) null else "android.resource://${context.packageName}/$id"
}

private fun criarCallbackDeDadosIniciais(context: Context) = object : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        dadosIniciais.forEach { categoria ->
            val valoresCategoria = ContentValues().apply {
                put("nome", categoria.nome)
                put("imagemUri", uriDaImagem(context, categoria.imagem))
            }
            val categoriaId = db.insert("categorias", SQLiteDatabase.CONFLICT_NONE, valoresCategoria)

            categoria.itens.forEach { item ->
                val valoresItem = ContentValues().apply {
                    put("categoriaId", categoriaId)
                    put("nome", item.nome)
                    put("descricao", item.descricao)
                    put("preco", item.preco)
                    put("imagemUri", uriDaImagem(context, item.imagem) ?: "")
                }
                db.insert("produtos", SQLiteDatabase.CONFLICT_NONE, valoresItem)
            }
        }
    }
}

@Database(
    entities = [Categoria::class, Produto::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoriaDao(): CategoriaDao
    abstract fun produtoDao(): ProdutoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obterInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cometashop.db"
                )
                    .addMigrations(MIGRACAO_1_2)
                    .addCallback(criarCallbackDeDadosIniciais(context.applicationContext))
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
