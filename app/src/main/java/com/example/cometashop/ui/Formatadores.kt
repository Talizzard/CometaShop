package com.example.cometashop.ui

import java.text.NumberFormat
import java.util.Locale

private val localeBrasil: Locale = Locale.Builder().setLanguage("pt").setRegion("BR").build()

fun formatarPreco(valor: Double): String {
    return NumberFormat.getCurrencyInstance(localeBrasil).format(valor)
}
