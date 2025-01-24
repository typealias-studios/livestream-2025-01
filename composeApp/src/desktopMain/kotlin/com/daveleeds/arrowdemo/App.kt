package com.daveleeds.arrowdemo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.daveleeds.arrowdemo.screens.WrestlerEditScreen
import com.daveleeds.arrowdemo.screens.WrestlerList
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
        Surface(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.WrestlerList
            ) {
                composable<Routes.WrestlerList> {
                    WrestlerList(
                        onWrestlerChosen = { id -> navController.navigate(Routes.WrestlerEdit(id)) }
                    )
                }

                composable<Routes.WrestlerEdit> { backStackEntry ->
                    val id = backStackEntry.toRoute<Routes.WrestlerEdit>().id
                    WrestlerEditScreen(id, onSaved = { navController.navigate(Routes.WrestlerList) })
                }
            }
        }
    }
}

object Routes {
    @Serializable object WrestlerList
    @Serializable data class WrestlerEdit(val id: Int)
}
