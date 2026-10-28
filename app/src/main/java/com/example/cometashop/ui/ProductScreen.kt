package com.example.cometashop.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clipToBounds
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
import com.example.cometashop.model.Categoria
import com.example.cometashop.model.Produto
import com.example.cometashop.ui.theme.*
import com.example.cometashop.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

enum class PrecoOpcao(val rotulo: String) {
    ATE_300("Menos de MC 300"),
    ATE_500("Menos de MC 500"),
    ATE_800("Menos de MC 800"),
    MAIS_800("Mais de MC 800")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
    categories: List<Categoria>,
    onBackClick: () -> Unit,
    onProductClick: (Int) -> Unit
) {
    val products by viewModel.products.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var produtoEmEdicao by remember { mutableStateOf<Produto?>(null) }

    LaunchedEffect(Unit) {
        viewModel.carregar()
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var appliedCategoryFilter by remember { mutableStateOf<Int?>(null) }
    var appliedPriceFilters by remember { mutableStateOf<Set<PrecoOpcao>>(emptySet()) }

    var draftCategoryFilter by remember { mutableStateOf<Int?>(null) }
    var draftPriceFilters by remember { mutableStateOf<Set<PrecoOpcao>>(emptySet()) }

    val filteredProducts = products.filter { product ->
        val matchesCategory = appliedCategoryFilter == null || product.categoriaId == appliedCategoryFilter

        val matchesPrice = if (appliedPriceFilters.isEmpty()) {
            true
        } else {
            appliedPriceFilters.any { opcao ->
                when (opcao) {
                    PrecoOpcao.ATE_300 -> product.preco < 300
                    PrecoOpcao.ATE_500 -> product.preco < 500
                    PrecoOpcao.ATE_800 -> product.preco < 800
                    PrecoOpcao.MAIS_800 -> product.preco >= 800
                }
            }
        }

        matchesCategory && matchesPrice
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.clipToBounds(),
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF1E2228),
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.FilterList, contentDescription = null, tint = CometaYellow)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "FILTROS DO ARSENAL",
                                color = CometaYellow,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.Gray)

                        Text(
                            text = "CATEGORIA",
                            color = CometaWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        var categoryDropdownExpanded by remember { mutableStateOf(false) }
                        val selectedCategoryName = categories.find { it.id == draftCategoryFilter }?.nome ?: "Todas as Categorias"

                        ExposedDropdownMenuBox(
                            expanded = categoryDropdownExpanded,
                            onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCategoryName,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = CometaWhite,
                                    unfocusedTextColor = CometaWhite,
                                    focusedContainerColor = Color(0xFF2A2E35),
                                    unfocusedContainerColor = Color(0xFF2A2E35)
                                ),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = categoryDropdownExpanded,
                                onDismissRequest = { categoryDropdownExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Todas as Categorias") },
                                    onClick = {
                                        draftCategoryFilter = null
                                        categoryDropdownExpanded = false
                                    }
                                )
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat.nome) },
                                        onClick = {
                                            draftCategoryFilter = cat.id
                                            categoryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "FAIXA DE PREÇO (MC)",
                            color = CometaWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        PrecoOpcao.values().forEach { opcao ->
                            val isChecked = draftPriceFilters.contains(opcao)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        draftPriceFilters = if (isChecked) {
                                            draftPriceFilters - opcao
                                        } else {
                                            draftPriceFilters + opcao
                                        }
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        draftPriceFilters = if (checked) {
                                            draftPriceFilters + opcao
                                        } else {
                                            draftPriceFilters - opcao
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = CometaYellow,
                                        checkmarkColor = CometaBlack,
                                        uncheckedColor = Color.Gray
                                    )
                                )
                                Text(
                                    text = opcao.rotulo,
                                    color = CometaWhite,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Column {
                        if (draftCategoryFilter != null || draftPriceFilters.isNotEmpty()) {
                            TextButton(
                                onClick = {
                                    draftCategoryFilter = null
                                    draftPriceFilters = emptySet()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("LIMPAR FILTROS", color = Color.LightGray)
                            }
                        }

                        Button(
                            onClick = {
                                appliedCategoryFilter = draftCategoryFilter
                                appliedPriceFilters = draftPriceFilters
                                scope.launch { drawerState.close() }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CometaYellow),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("SALVAR FILTROS", color = CometaBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Arsenal", color = CometaYellow, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = CometaYellow)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                draftCategoryFilter = appliedCategoryFilter
                                draftPriceFilters = appliedPriceFilters
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            BadgedBox(
                                badge = {
                                    if (appliedCategoryFilter != null || appliedPriceFilters.isNotEmpty()) {
                                        Badge(containerColor = CometaRed)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filtros", tint = CometaYellow)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = CometaBlack)
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showDialog = true }, containerColor = CometaRed) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = CometaWhite)
                }
            },
            containerColor = CometaBlack
        ) { paddingValues ->
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (products.isEmpty()) "Nenhum item no arsenal.\nClique no botão + para adicionar!" else "Nenhum item encontrado\ncom os filtros selecionados.",
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
                    items(filteredProducts) { product ->
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
                                    Text("MC: $${product.preco}", color = CometaWhite)
                                }
                                IconButton(onClick = { produtoEmEdicao = product }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = CometaYellow)
                                }
                                IconButton(onClick = { viewModel.removeProduct(product) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = CometaRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddProductDialog(
            categories = categories,
            onDismiss = { showDialog = false },
            onSave = { nome, catId, desc, preco, uri ->
                viewModel.addProduct(nome, catId, desc, preco, uri)
                showDialog = false
            }
        )
    }

    produtoEmEdicao?.let { produto ->
        AddProductDialog(
            categories = categories,
            produtoInicial = produto,
            onDismiss = { produtoEmEdicao = null },
            onSave = { nome, catId, desc, preco, uri ->
                viewModel.updateProduct(produto.id, nome, catId, desc, preco, uri)
                produtoEmEdicao = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    categories: List<Categoria>,
    onDismiss: () -> Unit,
    onSave: (String, Int, String, Double, String) -> Unit,
    produtoInicial: Produto? = null
) {
    var nome by remember { mutableStateOf(produtoInicial?.nome ?: "") }
    var descricao by remember { mutableStateOf(produtoInicial?.descricao ?: "") }
    var preco by remember { mutableStateOf(produtoInicial?.preco?.toString() ?: "") }

    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember {
        mutableStateOf(
            categories.find { it.id == produtoInicial?.categoriaId } ?: categories.firstOrNull()
        )
    }
    var selectedImageUri by remember { mutableStateOf<String?>(produtoInicial?.imagemUri) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? -> if (uri != null) selectedImageUri = uri.toString() }
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.DarkGray,
        title = {
            Text(
                if (produtoInicial == null) "Novo Item do Arsenal" else "Editar Item do Arsenal",
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
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
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

                OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome", color = CometaWhite) }, singleLine = true)

                ExposedDropdownMenuBox(expanded = expandedCategory, onExpandedChange = { expandedCategory = !expandedCategory }) {
                    OutlinedTextField(
                        value = selectedCategory?.nome ?: "Selecione a categoria",
                        onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedCategory, onDismissRequest = { expandedCategory = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(text = { Text(cat.nome) }, onClick = { selectedCategory = cat; expandedCategory = false })
                        }
                    }
                }

                OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço", color = CometaWhite) }, singleLine = true)
                OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição", color = CometaWhite) })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = preco.toDoubleOrNull() ?: 0.0
                    if (nome.isNotBlank() && selectedCategory != null && selectedImageUri != null) {
                        onSave(nome, selectedCategory!!.id, descricao, p, selectedImageUri!!)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CometaYellow)
            ) {
                Text(
                    if (produtoInicial == null) "CRIAR" else "SALVAR",
                    color = CometaBlack,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCELAR", color = CometaWhite) } }
    )
}
