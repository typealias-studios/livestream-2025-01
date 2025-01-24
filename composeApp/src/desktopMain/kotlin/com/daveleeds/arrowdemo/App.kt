package com.daveleeds.arrowdemo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import com.daveleeds.arrowdemo.theme.darkScheme
import com.daveleeds.arrowdemo.theme.lightScheme
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController(),
    darkTheme: Boolean = true,
) {
    MaterialTheme(colorScheme = if (darkTheme) darkScheme else lightScheme) {
        NavHost(
            navController = navController,
            startDestination = WrestlerList
        ) {
            composable<WrestlerList> {
                WrestlerList(
                    onWrestlerChosen = { id -> navController.navigate(WrestlerEdit(id)) }
                )
            }

            composable<WrestlerEdit> { backStackEntry ->
                val id = backStackEntry.toRoute<WrestlerEdit>().id
                WrestlerEditScreen(id, onSaved = { navController.navigate(WrestlerList) })
            }
        }
    }
}

@Composable
fun WrestlerList(
    viewModel: WrestlerListViewModel = viewModel { WrestlerListViewModel() },
    onWrestlerChosen: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.refresh()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.padding(16.dp)) {
            if (uiState.loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column {
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
            Text(wrestler.name, style = typography.headlineLarge, color = colorScheme.onSurface)
            Text("Age: ${wrestler.age} years", style = typography.bodyLarge, color = colorScheme.onSurface)
            Text("Weight: ${wrestler.weight} lbs", style = typography.bodyLarge, color = colorScheme.onSurface)
            Text("Hometown: ${wrestler.hometown.city}, ${wrestler.hometown.country}", style = typography.bodyLarge, color = colorScheme.onSurface)
        }
    }
}

@Composable
fun WrestlerEditScreen(
    id: Int,
    viewModel: WrestlerEditViewModel = viewModel { WrestlerEditViewModel() },
    onSaved: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.status) {
        when (uiState.status) {
            WrestlerEditStatus.FRESH -> viewModel.load(id)
            WrestlerEditStatus.SAVED -> onSaved()
            else                     -> {}
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Box(Modifier.padding(16.dp)) {
            if (uiState.status in listOf(WrestlerEditStatus.LOADING, WrestlerEditStatus.SAVING)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(verticalArrangement = spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
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
}

@Serializable
object WrestlerList

@Serializable
data class WrestlerEdit(val id: Int)
