package com.example.cometashop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.cometashop.ui.theme.CometaBlack
import com.example.cometashop.viewmodel.CartViewModel
import com.example.cometashop.viewmodel.StoreViewModel

@Composable
fun StoreCategoryScreen(
    viewModel: StoreViewModel,
    cartViewModel: CartViewModel,
    onProdutoClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarFiltroPreco by remember { mutableStateOf(false) }

    ComCarrinhoLateral(viewModel = cartViewModel) { abrirCarrinho ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CometaBlack)
                .navigationBarsPadding()
        ) {
            BarraSuperiorLoja(
                busca = uiState.buscaCategoria,
                aoMudarBusca = { viewModel.aoMudarBuscaCategoria(it) },
                aoClicarCarrinho = abrirCarrinho,
                aoClicarPreco = { mostrarFiltroPreco = true }
            )

            GradeDeProdutos(
                produtos = uiState.itensDaCategoriaFiltrados,
                mensagemVazia = if (uiState.produtosDaCategoria.isEmpty()) {
                    "Nenhum item nesta categoria ainda."
                } else {
                    "Nenhum item encontrado."
                },
                onProdutoClick = onProdutoClick
            )
        }
    }

    if (mostrarFiltroPreco) {
        FaixaPrecoDialog(
            limite = uiState.limitePreco,
            faixa = uiState.faixaEfetiva,
            aoMudar = { viewModel.aoMudarFaixaPreco(it) },
            aoLimpar = { viewModel.limparFaixaPreco() },
            aoFechar = { mostrarFiltroPreco = false }
        )
    }
}
