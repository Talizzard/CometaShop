package com.example.cometashop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cometashop.ui.theme.CometaBlack
import com.example.cometashop.ui.theme.CometaWhite
import com.example.cometashop.viewmodel.CartViewModel
import com.example.cometashop.viewmodel.StoreViewModel

@Composable
fun StoreHomeScreen(
    viewModel: StoreViewModel,
    cartViewModel: CartViewModel,
    onCategoriaClick: (Int) -> Unit,
    onProdutoClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ComCarrinhoLateral(viewModel = cartViewModel) { abrirCarrinho ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CometaBlack)
                .navigationBarsPadding()
        ) {
            BarraSuperiorLoja(
                busca = uiState.buscaLoja,
                aoMudarBusca = { viewModel.aoMudarBuscaLoja(it) },
                aoClicarCarrinho = abrirCarrinho
            )

            if (uiState.buscandoNaLoja) {
                GradeDeProdutos(
                    produtos = uiState.resultadosDaLoja,
                    mensagemVazia = "Nenhum item encontrado.",
                    onProdutoClick = onProdutoClick
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    if (uiState.destaques.isNotEmpty()) {
                        item { TituloSecao("Itens Destacados") }
                        item {
                            CarrosselDestaques(
                                produtos = uiState.destaques,
                                onProdutoClick = { onProdutoClick(it.id) }
                            )
                        }
                    }

                    item { TituloSecao("Categorias") }

                    if (uiState.categorias.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Nenhuma categoria cadastrada ainda.",
                                    color = CometaWhite,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        linhasDeDois(uiState.categorias) { categoria, modificador ->
                            CartaoCategoria(categoria = categoria, modifier = modificador) {
                                onCategoriaClick(categoria.id)
                            }
                        }
                    }
                }
            }
        }
    }
}
