package com.example.cometashop.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.cometashop.data.Categoria
import com.example.cometashop.data.Produto
import com.example.cometashop.ui.theme.CometaBlack
import com.example.cometashop.ui.theme.CometaWhite
import com.example.cometashop.ui.theme.CometaYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun CartaoCometa(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    conteudo: @Composable BoxScope.() -> Unit
) {
    val fonteInteracao = remember { MutableInteractionSource() }
    val pressionado by fonteInteracao.collectIsPressedAsState()
    var clicado by remember { mutableStateOf(false) }
    val escopo = rememberCoroutineScope()
    val destacado = pressionado || clicado
    val escala by animateFloatAsState(
        targetValue = if (destacado) 1.06f else 1f,
        animationSpec = tween(150),
        label = "escala"
    )
    val brilho by animateFloatAsState(
        targetValue = if (destacado) 1f else 0f,
        animationSpec = tween(150),
        label = "brilho"
    )
    val forma = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .zIndex(if (destacado) 1f else 0f)
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
            }
            .drawBehind {
                if (brilho > 0f) {
                    val raio = 12.dp.toPx()
                    for (camada in 1..6) {
                        val expansao = camada * 2.dp.toPx()
                        drawRoundRect(
                            color = CometaWhite.copy(alpha = 0.06f * brilho),
                            topLeft = Offset(-expansao, -expansao),
                            size = Size(size.width + expansao * 2, size.height + expansao * 2),
                            cornerRadius = CornerRadius(raio + expansao)
                        )
                    }
                }
            }
            .clip(forma)
            .background(CometaBlack)
            .border(1.dp, CometaWhite, forma)
            .clickable(interactionSource = fonteInteracao, indication = null) {
                if (!clicado) {
                    clicado = true
                    escopo.launch {
                        delay(160)
                        onClick()
                        clicado = false
                    }
                }
            },
        content = conteudo
    )
}

@Composable
fun CartaoCategoria(
    categoria: Categoria,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CartaoCometa(modifier = modifier.aspectRatio(1f), onClick = onClick) {
        if (categoria.imagemUri != null) {
            AsyncImage(
                model = categoria.imagemUri,
                contentDescription = categoria.nome,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CometaYellow.copy(alpha = 0.35f))
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.55f)
                .background(Brush.verticalGradient(colors = listOf(Color.Transparent, CometaBlack)))
        )
        Text(
            text = categoria.nome,
            color = CometaWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun CartaoProduto(
    produto: Produto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CartaoCometa(modifier = modifier.aspectRatio(1f), onClick = onClick) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = produto.nome,
                color = CometaWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )
            AsyncImage(
                model = produto.imagemUri,
                contentDescription = produto.nome,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = formatarPreco(produto.preco),
                color = CometaWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

fun <T> LazyListScope.linhasDeDois(
    lista: List<T>,
    conteudo: @Composable (T, Modifier) -> Unit
) {
    items(lista.chunked(2)) { par ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            if (par.size == 2) {
                conteudo(par[0], Modifier.weight(1f).padding(6.dp))
                conteudo(par[1], Modifier.weight(1f).padding(6.dp))
            } else {
                Spacer(modifier = Modifier.weight(0.5f))
                conteudo(par[0], Modifier.weight(1f).padding(6.dp))
                Spacer(modifier = Modifier.weight(0.5f))
            }
        }
    }
}

@Composable
fun GradeDeProdutos(
    produtos: List<Produto>,
    mensagemVazia: String,
    onProdutoClick: (Int) -> Unit
) {
    if (produtos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mensagemVazia,
                color = CometaWhite,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            linhasDeDois(produtos) { produto, modificador ->
                CartaoProduto(produto = produto, modifier = modificador) {
                    onProdutoClick(produto.id)
                }
            }
        }
    }
}

@Composable
fun TituloSecao(texto: String) {
    Text(
        text = texto,
        color = CometaWhite,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    )
}

@Composable
fun CarrosselDestaques(
    produtos: List<Produto>,
    onProdutoClick: (Produto) -> Unit
) {
    val total = produtos.size
    if (total == 0) return

    val progresso = remember { Animatable(0f) }

    LaunchedEffect(total) {
        if (total > 1) {
            while (true) {
                delay(5000)
                progresso.animateTo(progresso.value + 1f, animationSpec = tween(700))
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        produtos.forEachIndexed { indice, produto ->
            var distancia = ((indice - progresso.value) % total + total) % total
            if (distancia > total / 2f) distancia -= total
            val absoluta = abs(distancia)
            val fimDaVolta = total / 2f
            val desvanecer = when {
                absoluta <= 1f -> 1f
                fimDaVolta > 1f -> ((fimDaVolta - absoluta) / (fimDaVolta - 1f)).coerceIn(0f, 1f)
                else -> 0f
            }
            val parcial = minOf(absoluta, 1f)
            val transparencia = (1f - 0.3f * parcial) * desvanecer
            val escala = 1f - 0.22f * parcial

            if (transparencia > 0.01f) {
                Box(
                    modifier = Modifier
                        .zIndex(10f - absoluta)
                        .offset(x = (distancia * 125f).dp)
                        .graphicsLayer {
                            scaleX = escala
                            scaleY = escala
                            alpha = transparencia
                        }
                        .size(190.dp)
                ) {
                    CartaoProduto(
                        produto = produto,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onProdutoClick(produto) }
                    )
                }
            }
        }
    }
}
