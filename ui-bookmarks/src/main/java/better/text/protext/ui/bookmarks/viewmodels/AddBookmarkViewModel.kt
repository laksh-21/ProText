package better.text.protext.ui.bookmarks.viewmodels

import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.bookmarks.GetWebsiteTitleByUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookmarkViewModel @Inject constructor(
    private val getWebsiteTitleByUrlUseCase: GetWebsiteTitleByUrlUseCase
) : BaseViewModel() {
    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title: StateFlow<String> = _title
    fun getWebsiteTitle(url: String) {
        viewModelScope.launch {
            getWebsiteTitleByUrlUseCase(
                GetWebsiteTitleByUrlUseCase.Params(
                    url = url
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> _title.emit(it.data)
                    else -> {}
                }
            }
        }
    }
}
