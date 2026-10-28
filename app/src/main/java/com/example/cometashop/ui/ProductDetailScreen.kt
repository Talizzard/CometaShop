package com.example.cometashop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cometashop.ui.theme.*
import com.example.cometashop.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: ProductViewModel,
    onBackClick: () -> Unit
) {
    val product by viewModel.produtoSelecionado.collectAsState()
    val carregado by viewModel.detalheCarregado.collectAsState()

    LaunchedEffect(productId) {
        viewModel.carregarProduto(productId)
    }

    val item = product
    if (item == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CometaBlack),
            contentAlignment = Alignment.Center
        ) {
            if (carregado) {
                Text("Item não encontrado.", color = CometaWhite)
            } else {
                CircularProgressIndicator(color = CometaYellow)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.nome, color = CometaYellow, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = CometaYellow)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CometaBlack)
            )
        },
        containerColor = CometaBlack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = item.imagemUri,
                contentDescription = item.nome,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.DarkGray)
            )

            Text(
                text = "MC: $${item.preco}",
                color = CometaYellow,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "ESPECIFICAÇÕES TÁTICAS",
                color = CometaWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = item.descricao.ifBlank { "Nenhuma especificação cadastrada para este item." },
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}
