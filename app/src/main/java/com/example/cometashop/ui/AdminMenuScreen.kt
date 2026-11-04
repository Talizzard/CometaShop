package com.example.cometashop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cometashop.ui.theme.*

@Composable
fun AdminMenuScreen(
    onNavigateToCategories: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CometaBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ÁREA DO\nADMINISTRADOR",
            color = CometaYellow,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onNavigateToCategories() },
            colors = ButtonDefaults.buttonColors(containerColor = CometaRed),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("GERENCIAR CATEGORIAS", color = CometaWhite, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNavigateToProducts() },
            colors = ButtonDefaults.buttonColors(containerColor = CometaYellow),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("GERENCIAR ITENS", color = CometaBlack, fontWeight = FontWeight.Bold)
        }
    }
}
