package net.matsudamper.shareoutside.bluebird

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.matsudamper.shareoutside.bluebird.compose.MainScreenUiState
import net.matsudamper.shareoutside.bluebird.lib.ViewModelEventHandler
import net.matsudamper.shareoutside.bluebird.lib.ViewModelEventSender
import java.net.URL
import java.net.URLDecoder

public class MainActivityViewModel : ViewModel() {
    private val viewModelStateFlow: MutableStateFlow<ViewModelState> = MutableStateFlow(
        ViewModelState(
            shareTextValue = TextFieldValue(),
        ),
    )
    private val viewModelEventSender: ViewModelEventSender<Event> = ViewModelEventSender()
    public val viewModelEventHandler: ViewModelEventHandler<Event> = viewModelEventSender.asHandler()

    public val uiStateFlow: StateFlow<MainScreenUiState> = MutableStateFlow(
        MainScreenUiState(
            shareTextValue = TextFieldValue(),
            event = object : MainScreenUiState.Event {
                override fun onShareTextValueChanged(value: TextFieldValue) {
                    viewModelStateFlow.update {
                        it.copy(
                            shareTextValue = value,
                        )
                    }
                }

                override fun onClickHelp() {

                }

                override fun onClickShare() {
                    viewModelScope.launch {
                        viewModelEventSender.send {
                            it.share(viewModelStateFlow.value.shareTextValue.text)
                        }
                    }
                }
            },
        ),
    ).also { uiStateFlow ->
        viewModelScope.launch {
            viewModelStateFlow.collect { viewModelState ->
                uiStateFlow.update { uiState ->
                    uiState.copy(
                        shareTextValue = viewModelState.shareTextValue,
                    )
                }
            }
        }
    }.asStateFlow()

    /**
     * @return handled
     */
    public fun handleDataString(dataString: String?) : Boolean {
        dataString ?: return false
        val parseResult = runCatching {
            URL(dataString).query.split("&").associate { param ->
                param.split("=").let { keyValue ->
                    URLDecoder.decode(keyValue[0]) to
                            URLDecoder.decode(keyValue[1])
                }
            }
        }.map {
            listOfNotNull(
                it["text"],
                it["url"],
            ).joinToString("\n")
        }.getOrNull()

        if (parseResult != null) {
            updateText(parseResult)
            return true
        }

        return false
    }

    private fun updateText(text: String) {
        viewModelStateFlow.update {
            it.copy(
                shareTextValue = TextFieldValue(text),
            )
        }
    }

    public interface Event {
        public fun share(text: String)
    }

    private data class ViewModelState(
        val shareTextValue: TextFieldValue,
    )
}