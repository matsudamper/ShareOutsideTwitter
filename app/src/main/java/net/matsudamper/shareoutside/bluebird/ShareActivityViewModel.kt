package net.matsudamper.shareoutside.bluebird

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.net.URL
import java.net.URLDecoder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.matsudamper.shareoutside.bluebird.compose.ShareScreenUiState
import net.matsudamper.shareoutside.bluebird.lib.ViewModelEventHandler
import net.matsudamper.shareoutside.bluebird.lib.ViewModelEventSender

public class ShareActivityViewModel : ViewModel() {
    private val viewModelStateFlow: MutableStateFlow<ViewModelState> = MutableStateFlow(
        ViewModelState(
            shareTextValue = TextFieldValue(),
        ),
    )
    private val viewModelEventSender: ViewModelEventSender<Event> = ViewModelEventSender()
    public val viewModelEventHandler: ViewModelEventHandler<Event> = viewModelEventSender.asHandler()

    public val uiStateFlow: StateFlow<ShareScreenUiState> = MutableStateFlow(
        ShareScreenUiState(
            shareTextValue = TextFieldValue(),
            event = object : ShareScreenUiState.Event {
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
    public fun handleDataString(dataString: String?): Boolean {
        dataString ?: return false
        val parseResult = runCatching {
            val url = URL(dataString)
            val params = getParams(url)
            when (url.path) {
                "/intent/tweet" -> {
                    listOfNotNull(
                        params["text"],
                        params["url"],
                    ).joinToString("\n")
                }

                "/share" -> {
                    listOfNotNull(
                        params["text"],
                        params["url"],
                        params["via"]?.let {
                            "via $it"
                        }
                    ).joinToString("\n")
                }

                else -> return false
            }
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()

        if (parseResult != null) {
            updateText(parseResult)
            return true
        }

        return false
    }

    private fun getParams(url: URL): Map<String, String> {
        return url.query.split("&").associate { param ->
            param.split("=").let { keyValue ->
                URLDecoder.decode(keyValue[0]) to
                        URLDecoder.decode(keyValue.getOrNull(1).orEmpty())
            }
        }
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