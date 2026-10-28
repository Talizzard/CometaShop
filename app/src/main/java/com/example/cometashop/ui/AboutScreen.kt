package com.example.cometashop.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cometashop.R
import com.example.cometashop.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sobre Nós", color = CometaYellow, fontWeight = FontWeight.Bold) },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.equipe),
                contentDescription = "Imagem da Equipe",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.DarkGray)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "A EQUIPE DESENVOLVEDORA",
                color = CometaYellow,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Somos a Cometa, a principal fornecedora de equipamentos táticos, skins de alta performance e arsenais pesados para os competidores de The Finals.\n" +
                        "\n" +
                        "Nossa missão é garantir que você tenha o equipamento certo para destruir a arena, proteger o seu cofre e alcançar a glória sob os holofotes.\n" +
                        "\n" +
                        "TEMOS: Tales Mácola, Gustavo Medina e Henrique Funes. \nA equipe mais badalada de todos os tempos!!",
                color = CometaWhite,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}
