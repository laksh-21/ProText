package better.text.protext.base.baseScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val eventsChannel = Channel<UIEvent>(Channel.BUFFERED)
    val eventsFlow = eventsChannel.receiveAsFlow()
    fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            eventsChannel.send(event)
        }
    }
}
