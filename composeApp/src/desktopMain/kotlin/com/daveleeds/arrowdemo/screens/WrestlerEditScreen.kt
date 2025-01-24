package com.daveleeds.arrowdemo.screens

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daveleeds.arrowdemo.viewmodel.WrestlerEditStatus
import com.daveleeds.arrowdemo.viewmodel.WrestlerEditViewModel

@Composable
fun WrestlerEditScreen(
    id: Int,
    viewModel: WrestlerEditViewModel = viewModel { WrestlerEditViewModel() },
    onSaved: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.status) {
        when (uiState.status) {
            WrestlerEditStatus.START -> viewModel.load(id)
            WrestlerEditStatus.SAVED -> onSaved()
            else                     -> {}
        }
    }

    Box(Modifier.padding(16.dp)) {
        if (uiState.status in listOf(WrestlerEditStatus.LOADING, WrestlerEditStatus.SAVING)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                verticalArrangement = spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                uiState.exception?.let { e -> Text("Error: ${e.message}", color = colorScheme.error) }

                TextField(
                    value = uiState.wrestler?.name.orEmpty(),
                    onValueChange = { viewModel.setName(it) },
                    label = { Text("Name") }
                )
                TextField(
                    value = uiState.wrestler?.age?.toString().orEmpty(),
                    onValueChange = { it.toIntOrNull()?.let { viewModel.setAge(it) } },
                    label = { Text("Age") }
                )
                TextField(
                    value = uiState.wrestler?.weight?.toString().orEmpty(),
                    onValueChange = { it.toIntOrNull()?.let { viewModel.setWeight(it) } },
                    label = { Text("Weight") }
                )
                TextField(
                    value = uiState.wrestler?.hometown?.city.orEmpty(),
                    onValueChange = { viewModel.setCity(it) },
                    label = { Text("City") }
                )
                TextField(
                    value = uiState.wrestler?.hometown?.country.orEmpty(),
                    onValueChange = { viewModel.setCountry(it) },
                    label = { Text("Country") }
                )
                Button(onClick = { viewModel.save() }, modifier = Modifier.align(Alignment.End)) {
                    Text("Save")
                }
            }
        }
    }
}

