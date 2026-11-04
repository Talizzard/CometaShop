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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cometashop.ui.theme.*
import com.example.cometashop.viewmodel.CategoryViewModel

@Composable
private fun SeletorDeImagem(
    uri: String?,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            AsyncImage(
                model = uri,
                contentDescription = "Imagem da categoria",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(Icons.Default.Image, contentDescription = "Escolher imagem", tint = CometaWhite)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val seletorNova = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? -> if (uri != null) viewModel.aoEscolherImagemNova(uri.toString()) }
    )
    val seletorEdicao = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? -> if (uri != null) viewModel.aoEscolherImagemEditada(uri.toString()) }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorias", color = CometaYellow, fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SeletorDeImagem(
                    uri = uiState.imagemNova,
                    modifier = Modifier.size(56.dp),
                    onClick = {
                        seletorNova.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = uiState.nomeNovaCategoria,
                    onValueChange = { viewModel.aoMudarNomeNovaCategoria(it) },
                    label = { Text("Nova Categoria", color = CometaWhite) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CometaWhite,
                        unfocusedTextColor = CometaWhite,
                        focusedBorderColor = CometaYellow,
                        unfocusedBorderColor = CometaWhite
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.adicionarCategoria() },
                    colors = ButtonDefaults.buttonColors(containerColor = CometaRed)
                ) {
                    Text("CRIAR", color = CometaWhite, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.categorias) { category ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Gray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (category.imagemUri != null) {
                                        AsyncImage(
                                            model = category.imagemUri,
                                            contentDescription = category.nome,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Icon(Icons.Default.Image, contentDescription = null, tint = CometaWhite)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = category.nome, color = CometaWhite, fontWeight = FontWeight.Bold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.iniciarEdicao(category) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = CometaYellow)
                                }
                                IconButton(onClick = { viewModel.removerCategoria(category) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = CometaRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (uiState.categoriaEmEdicao != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelarEdicao() },
            containerColor = Color.DarkGray,
            title = { Text("Editar Categoria", color = CometaYellow) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SeletorDeImagem(
                        uri = uiState.imagemEditada,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        onClick = {
                            seletorEdicao.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                    OutlinedTextField(
                        value = uiState.nomeEditado,
                        onValueChange = { viewModel.aoMudarNomeEditado(it) },
                        label = { Text("Nome", color = CometaWhite) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CometaWhite,
                            unfocusedTextColor = CometaWhite,
                            focusedBorderColor = CometaYellow,
                            unfocusedBorderColor = CometaWhite
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.salvarEdicao() },
                    colors = ButtonDefaults.buttonColors(containerColor = CometaYellow)
                ) { Text("SALVAR", color = CometaBlack, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelarEdicao() }) {
                    Text("CANCELAR", color = CometaWhite)
                }
            }
        )
    }
}
