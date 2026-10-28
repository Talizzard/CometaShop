package com.example.cometashop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cometashop.model.Categoria
import com.example.cometashop.ui.theme.*
import com.example.cometashop.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onBackClick: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    var categoryName by remember { mutableStateOf("") }
    var categoriaEmEdicao by remember { mutableStateOf<Categoria?>(null) }
    var nomeEditado by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.carregar()
    }

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
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    label = { Text("Nova Categoria", color = CometaWhite) },
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
                    onClick = {
                        viewModel.addCategory(categoryName)
                        categoryName = ""
                    },
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
                items(categories) { category ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = category.nome, color = CometaWhite, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    nomeEditado = category.nome
                                    categoriaEmEdicao = category
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = CometaYellow)
                                }
                                IconButton(onClick = { viewModel.removeCategory(category) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = CometaRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    categoriaEmEdicao?.let { categoria ->
        AlertDialog(
            onDismissRequest = { categoriaEmEdicao = null },
            containerColor = Color.DarkGray,
            title = { Text("Editar Categoria", color = CometaYellow) },
            text = {
                OutlinedTextField(
                    value = nomeEditado,
                    onValueChange = { nomeEditado = it },
                    label = { Text("Nome", color = CometaWhite) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CometaWhite,
                        unfocusedTextColor = CometaWhite,
                        focusedBorderColor = CometaYellow,
                        unfocusedBorderColor = CometaWhite
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateCategory(categoria, nomeEditado)
                        categoriaEmEdicao = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CometaYellow)
                ) { Text("SALVAR", color = CometaBlack, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { categoriaEmEdicao = null }) {
                    Text("CANCELAR", color = CometaWhite)
                }
            }
        )
    }
}
