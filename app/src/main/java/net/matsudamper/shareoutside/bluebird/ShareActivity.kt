package net.matsudamper.shareoutside.bluebird

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import net.matsudamper.shareoutside.bluebird.compose.ShareScreen

public class ShareActivity() : ComponentActivity() {
    private val viewModel: ShareActivityViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory() {
            override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                @Suppress("UNCHECKED_CAST")
                return ShareActivityViewModel() as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        handleIntent(intent)
        setContent {
            LaunchedEffect(viewModel.viewModelEventHandler) {
                viewModel.viewModelEventHandler.collect(viewModelEvent)
            }
            ShareScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = viewModel.uiStateFlow.collectAsState().value,
            )
        }
    }

    private val viewModelEvent = object : ShareActivityViewModel.Event {
        override fun share(text: String) {
            startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, text)
                    },
                    null,
                ),
            )
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    @SuppressLint("UnsafeIntentLaunch")
    private fun handleIntent(intent: Intent?) {
        intent ?: return

        val handled = viewModel.handleDataString(intent.dataString)
        if (handled.not()) {
            startActivity(
                intent.also { intent ->
                    intent.setComponent(null)
                    intent.setPackage("com.twitter.android")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                },
            )
            finish()
        }
    }
}