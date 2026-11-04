package com.example.cometashop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cometashop.ui.theme.CometaBlack
import com.example.cometashop.ui.theme.CometaRed
import com.example.cometashop.ui.theme.CometaWhite
import com.example.cometashop.ui.theme.CometaYellow

@Composable
fun BarraSuperiorLoja(
    busca: String,
    aoMudarBusca: (String) -> Unit,
    aoClicarCarrinho: () -> Unit,
    aoClicarPreco: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CometaYellow)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (aoClicarPreco != null) {
            IconButton(
                onClick = aoClicarPreco,
                modifier = Modifier
                    .size(44.dp)
                    .border(2.dp, CometaRed, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "Filtrar por preço",
                    tint = CometaRed
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        OutlinedTextField(
            value = busca,
            onValueChange = aoMudarBusca,
            placeholder = { Text("Pesquisar", color = CometaWhite.copy(alpha = 0.6f)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Pesquisar",
                    tint = CometaRed
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = CometaWhite,
                unfocusedTextColor = CometaWhite,
                focusedContainerColor = CometaBlack,
                unfocusedContainerColor = CometaBlack,
                focusedBorderColor = CometaRed,
                unfocusedBorderColor = CometaRed,
                cursorColor = CometaRed
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = aoClicarCarrinho) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrinho",
                tint = CometaRed
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaixaPrecoDialog(
    limite: Float,
    faixa: ClosedFloatingPointRange<Float>,
    aoMudar: (ClosedFloatingPointRange<Float>) -> Unit,
    aoLimpar: () -> Unit,
    aoFechar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = aoFechar,
        containerColor = CometaBlack,
        title = {
            Text("Faixa de preço", color = CometaWhite, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    text = "${formatarPreco(faixa.start.toDouble())}  até  ${formatarPreco(faixa.endInclusive.toDouble())}",
                    color = CometaWhite
                )
                Spacer(modifier = Modifier.height(12.dp))
                RangeSlider(
                    value = faixa,
                    onValueChange = aoMudar,
                    valueRange = 0f..limite,
                    colors = SliderDefaults.colors(
                        thumbColor = CometaRed,
                        activeTrackColor = CometaRed,
                        inactiveTrackColor = CometaWhite.copy(alpha = 0.3f)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = aoFechar,
                colors = ButtonDefaults.buttonColors(containerColor = CometaRed)
            ) {
                Text("OK", color = CometaWhite, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = aoLimpar) {
                Text("LIMPAR", color = CometaWhite)
            }
        }
    )
}
