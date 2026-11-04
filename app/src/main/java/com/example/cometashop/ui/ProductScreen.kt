package com.example.cometashop.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cometashop.data.Categoria
import com.example.cometashop.ui.theme.*
import com.example.cometashop.viewmodel.ProductUiState
import com.example.cometashop.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
    categories: List<Categoria>,
    onBackClick: () -> Unit,
    onProductClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Itens", color = CometaYellow, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = CometaYellow)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CometaBlack)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.abrirDialogoNovo(categories.firstOrNull()?.id) },
                containerColor = CometaRed
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = CometaWhite)
            }
        },
        containerColor = CometaBlack
    ) { paddingValues ->
        if (uiState.produtos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum item cadastrado.\nClique no botão + para adicionar!",
                    color = CometaWhite,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.produtos) { product ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProductClick(product.id) }
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = product.imagemUri,
                                contentDescription = "Imagem do Produto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.nome, color = CometaYellow, fontWeight = FontWeight.Bold)
                                Text(
                                    text = categories.find { it.id == product.categoriaId }?.nome ?: "",
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                                Text(formatarPreco(product.preco), color = CometaWhite)
                            }
                            IconButton(onClick = { viewModel.abrirDialogoEdicao(product) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = CometaYellow)
                            }
                            IconButton(onClick = { viewModel.removerProduto(product) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = CometaRed)
                            }
                        }
                    }
                }
            }
        }
    }

    if (uiState.dialogoAberto) {
        AddProductDialog(
            categories = categories,
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    categories: List<Categoria>,
    uiState: ProductUiState,
    viewModel: ProductViewModel
) {
    var expandedCategory by remember { mutableStateOf(false) }
    val selectedCategory = categories.find { it.id == uiState.formCategoriaId }
    val editando = uiState.produtoEmEdicaoId != null

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? -> if (uri != null) viewModel.aoEscolherImagem(uri.toString()) }
    )

    AlertDialog(
        onDismissRequest = { viewModel.fecharDialogo() },
        containerColor = Color.DarkGray,
        title = {
            Text(
                if (!editando) "Novo Item" else "Editar Item",
                color = CometaYellow
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.formImagemUri != null) {
                        AsyncImage(
                            model = uiState.formImagemUri,
                            contentDescription = "Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = CometaWhite)
                            Text("Toque para escolher da galeria", color = CometaWhite)
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.formNome,
                    onValueChange = { viewModel.aoMudarNome(it) },
                    label = { Text("Nome", color = CometaWhite) },
                    singleLine = true
                )

                ExposedDropdownMenuBox(expanded = expandedCategory, onExpandedChange = { expandedCategory = !expandedCategory }) {
                    OutlinedTextField(
                        value = selectedCategory?.nome ?: "Selecione a categoria",
                        onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedCategory, onDismissRequest = { expandedCategory = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.nome) },
                                onClick = {
                                    viewModel.aoEscolherCategoria(cat.id)
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.formPreco,
                    onValueChange = { viewModel.aoMudarPreco(it) },
                    label = { Text("Preço (R$)", color = CometaWhite) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = uiState.formDescricao,
                    onValueChange = { viewModel.aoMudarDescricao(it) },
                    label = { Text("Descrição", color = CometaWhite) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.salvarProduto() },
                colors = ButtonDefaults.buttonColors(containerColor = CometaYellow)
            ) {
                Text(
                    if (!editando) "CRIAR" else "SALVAR",
                    color = CometaBlack,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = { TextButton(onClick = { viewModel.fecharDialogo() }) { Text("CANCELAR", color = CometaWhite) } }
    )
}
