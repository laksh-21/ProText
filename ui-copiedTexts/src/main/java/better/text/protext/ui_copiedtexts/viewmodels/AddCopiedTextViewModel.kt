package better.text.protext.ui_copiedtexts.viewmodels

import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.copiedTexts.GetCopiedTextUseCase
import better.text.protext.localdata.database.entities.CopiedText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCopiedTextViewModel @Inject constructor(
    private val getCopiedTextUseCase: GetCopiedTextUseCase
) : BaseViewModel() {
    private val _copiedText = MutableStateFlow(
        CopiedText(
            id = -1L,
            text = "",
            lastUpdated = 0L
        )
    )
    val copiedText: StateFlow<CopiedText> = _copiedText

    fun getCopiedText(id: Long) {
        viewModelScope.launch {
            getCopiedTextUseCase(
                GetCopiedTextUseCase.Params(
                    id = id
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> _copiedText.emit(it.data)
                    else -> {}
                }
            }
        }
    }
}
