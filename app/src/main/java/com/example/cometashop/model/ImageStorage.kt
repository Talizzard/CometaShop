package com.example.cometashop.model

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

object ImageStorage {

    private fun pasta(context: Context): File =
        File(context.filesDir, "imagens").apply { mkdirs() }

    fun copiar(context: Context, uriOriginal: String): String {
        val destino = File(pasta(context), "${UUID.randomUUID()}.jpg")
        val entrada = context.contentResolver.openInputStream(Uri.parse(uriOriginal))
            ?: return uriOriginal
        entrada.use { input ->
            destino.outputStream().use { output -> input.copyTo(output) }
        }
        return Uri.fromFile(destino).toString()
    }

    fun apagar(context: Context, uriSalva: String) {
        runCatching {
            val arquivo = File(Uri.parse(uriSalva).path ?: return)
            if (arquivo.parentFile == pasta(context)) arquivo.delete()
        }
    }
}
