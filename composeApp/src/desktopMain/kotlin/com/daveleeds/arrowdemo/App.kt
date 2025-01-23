package com.daveleeds.arrowdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.daveleeds.arrowdemo.theme.darkScheme
import com.daveleeds.arrowdemo.theme.lightScheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: WrestlerViewModel, darkTheme: Boolean = true) {
    val uiState by viewModel.uiState.collectAsState()

    MaterialTheme(colorScheme = if (darkTheme) darkScheme else lightScheme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box {
                if (uiState.loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Column {
                        uiState.wrestlers.forEach { wrestler -> Wrestler(wrestler) }
                    }
                }
                Button(onClick = { viewModel.refresh() }, modifier = Modifier.padding(12.dp).align(Alignment.BottomEnd)) {
                    Text("Refresh")
                }
            }
        }
    }
}

@Composable
fun Wrestler(wrestler: Wrestler) {
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        AsyncImage(
            model = "$IMAGE_URL/${wrestler.id}.png",
            contentDescription = "${wrestler.name} profile picture",
            modifier = Modifier.size(128.dp)
        )
        Column(modifier = Modifier.weight(1.0f)) {
            Text(wrestler.name, style = typography.headlineLarge, color = colorScheme.onSurface)
            Text("Age: ${wrestler.age} years", style = typography.bodyLarge, color = colorScheme.onSurface)
            Text("Weight: ${wrestler.weight} lbs", style = typography.bodyLarge, color = colorScheme.onSurface)
            Text("Hometown: ${wrestler.hometown.city}, ${wrestler.hometown.country}", style = typography.bodyLarge, color = colorScheme.onSurface)
        }
    }

}

