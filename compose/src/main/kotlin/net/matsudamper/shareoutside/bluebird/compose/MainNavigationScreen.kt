package net.matsudamper.shareoutside.bluebird.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.matsudamper.shareoutside.bluebird.compose.theme.ShareOutsideTwitterTheme


@Composable
public fun MainNavigationScreen(
    mainNavigationScreenUiState: MainNavigationScreenUiState,
    helpScreenUiStateProvider: @Composable () -> HelpScreenUiState,
) {
    val navController = rememberNavController()
    val mainScreenNavController = rememberMainScreenNavController(navController)

    ShareOutsideTwitterTheme {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = MainScreenPage.Home.path,
        ) {
            composable(MainScreenPage.Home.path) {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    mainScreenNavController = mainScreenNavController,
                )
            }
            composable(MainScreenPage.Help.path) {
                val uiState = helpScreenUiStateProvider()
                HelpScreen(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState,
                )
            }
        }
    }
}

public data class MainNavigationScreenUiState(
    val event: Event,
) {
    @Immutable
    public interface Event
}

internal enum class MainScreenPage(val path: String) {
    Home("/"),
    Help("/help"),
}

internal interface MainScreenNavController {
    fun navigateToHelp()
    fun popBackStack()
}

@Composable
internal fun rememberMainScreenNavController(
    navController: NavController,
): MainScreenNavController {
    return remember(navController) {
        object : MainScreenNavController {
            override fun navigateToHelp() {
                navController.navigate(MainScreenPage.Help.path)
            }

            override fun popBackStack() {
                navController.popBackStack()
            }
        }
    }
}
