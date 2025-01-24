package com.daveleeds.arrowdemo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.daveleeds.arrowdemo.IMAGE_URL
import com.daveleeds.arrowdemo.Wrestler
import com.daveleeds.arrowdemo.viewmodel.WrestlerListStatus
import com.daveleeds.arrowdemo.viewmodel.WrestlerListViewModel

@Composable
fun WrestlerList(
    viewModel: WrestlerListViewModel = viewModel { WrestlerListViewModel() },
    onWrestlerChosen: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) { viewModel.refresh() }

    Box(Modifier.padding(16.dp)) {
        if (uiState.status == WrestlerListStatus.LOADING) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column {
                uiState.exception?.let { e ->
                    ErrorMessage(e.message, onTryAgain = { viewModel.refresh() })
                }
                uiState.wrestlers.forEach { wrestler ->
                    WrestlerRow(
                        wrestler = wrestler,
                        onClick = { onWrestlerChosen(wrestler.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(message: String?, onTryAgain: () -> Unit) {
    Row {
        Text("Error: $message", color = colorScheme.error)
        Spacer(Modifier.width(8.dp))
        Text(
            "Try again.",
            textDecoration = TextDecoration.Underline,
            color = colorScheme.error,
            modifier = Modifier.clickable { onTryAgain() }
        )
    }
}

@Composable
fun WrestlerRow(wrestler: Wrestler, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp).clickable { onClick() }) {
        AsyncImage(
            model = "$IMAGE_URL/${wrestler.id}.png",
            contentDescription = "${wrestler.name} profile picture",
            modifier = Modifier.size(128.dp)
        )
        Column(modifier = Modifier.weight(1.0f)) {
            Text(wrestler.name, style = typography.headlineLarge)
            Text("Age: ${wrestler.age} years", style = typography.bodyLarge)
            Text("Weight: ${wrestler.weight} lbs", style = typography.bodyLarge)
            Text("Hometown: ${wrestler.hometown.city}, ${wrestler.hometown.country}", style = typography.bodyLarge)
        }
    }
}
