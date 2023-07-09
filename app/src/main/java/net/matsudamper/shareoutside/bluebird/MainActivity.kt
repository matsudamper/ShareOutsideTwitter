package net.matsudamper.shareoutside.bluebird

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import net.matsudamper.shareoutside.bluebird.compose.HelpScreenUiState
import net.matsudamper.shareoutside.bluebird.compose.MainNavigationScreen
import net.matsudamper.shareoutside.bluebird.compose.MainNavigationScreenUiState


public class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MainNavigationScreen(
                mainNavigationScreenUiState = remember {
                    MainNavigationScreenUiState(
                        event = object : MainNavigationScreenUiState.Event {
                        },
                    )
                },
                helpScreenUiStateProvider = {
                    remember {
                        HelpScreenUiState(
                            event = object : HelpScreenUiState.Event {
                                override fun onClickBack() {
                                    onBackPressedDispatcher.onBackPressed()
                                }
                            },
                        )
                    }
                },
            )
        }
    }
}
