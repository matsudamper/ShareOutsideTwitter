package net.matsudamper.shareoutside.bluebird.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import net.matsudamper.shareoutside.bluebird.compose.theme.ShareOutsideTwitterTheme

public data class MainScreenUiState(
    val shareTextValue: TextFieldValue,
    val event: Event,
) {
    @Immutable
    public interface Event {
        public fun onShareTextValueChanged(value: TextFieldValue)
        public fun onClickHelp()
        public fun onClickShare()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainScreenUiState,
) {
    ShareOutsideTwitterTheme {
        Scaffold(
            modifier = modifier.imePadding(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Share")
                    },
                    actions = {
                        IconButton(onClick = { uiState.event.onClickHelp() }) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Share",
                            )
                        }
                    },
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    actions = {

                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { uiState.event.onClickShare() },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Share",
                            )
                        }
                    },
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = uiState.shareTextValue,
                    onValueChange = { uiState.event.onShareTextValueChanged(it) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MainScreen(
        uiState = MainScreenUiState(
            shareTextValue = TextFieldValue(),
            event = object : MainScreenUiState.Event {
                override fun onShareTextValueChanged(value: TextFieldValue) {}
                override fun onClickHelp() {}
                override fun onClickShare() {}
            }
        )
    )
}
