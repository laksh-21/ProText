package better.text.protext.ui_copiedtexts.viewmodels

import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.copiedTexts.AddCopiedTextUseCase
import better.text.protext.localdata.database.entities.CopiedText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CopiedTextServiceViewModel @Inject constructor(
    private val addCopiedTextUseCase: AddCopiedTextUseCase
) : BaseViewModel() {
    var copiedText: String = ""

    fun addCopiedText() {
        viewModelScope.launch {
            addCopiedTextUseCase(
                AddCopiedTextUseCase.Params(
                    CopiedText(
                        id = 0L,
                        text = copiedText.trim(),
                        lastUpdated = Date().time
                    )
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> {
                        sendEvent(UIEvent.ShowToast("Copied to Clipboard and Saved"))
                        sendEvent(UIEvent.GoBack)
                    }
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong"))
                    else -> {}
                }
            }
        }
    }
}
