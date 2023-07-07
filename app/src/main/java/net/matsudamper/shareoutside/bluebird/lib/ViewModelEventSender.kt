package net.matsudamper.shareoutside.bluebird.lib

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

public class ViewModelEventHandler<EventReceiver>(
    private val events: Channel<suspend (EventReceiver) -> Unit>,
) {
    public suspend fun collect(receiver: EventReceiver) {
        coroutineScope {
            events.receiveAsFlow().collect { event ->
                launch {
                    event.invoke(receiver)
                }
            }
        }
    }

    public suspend fun waitConsumeEvents() {
        @Suppress("OPT_IN_USAGE")
        while (events.isEmpty.not()) {
            delay(10)
        }
    }
}

public class ViewModelEventSender<EventReceiver> {
    private val events: Channel<suspend (EventReceiver) -> Unit> = Channel(Channel.UNLIMITED)

    public suspend fun <R> send(block: suspend (EventReceiver) -> R): R {
        val scope = CoroutineScope(coroutineContext)
        return suspendCoroutine { continuation ->
            scope.launch {
                events.send {
                    continuation.resume(block(it))
                }
            }
        }
    }

    public fun asHandler(): ViewModelEventHandler<EventReceiver> = ViewModelEventHandler(events)
}
