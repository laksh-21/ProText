package better.text.protext.ui_copiedtexts.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.copiedTexts.AddCopiedTextUseCase
import better.text.protext.interactors.copiedTexts.DeleteCopiedTextUseCase
import better.text.protext.interactors.copiedTexts.GetAllCopiedTextsUseCase
import better.text.protext.interactors.copiedTexts.UpdateCopiedTextUseCase
import better.text.protext.localdata.database.entities.CopiedText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CopiedTextViewModel @Inject constructor(
    private val getAllCopiedTextsUseCase: GetAllCopiedTextsUseCase,
    private val addCopiedTextUseCase: AddCopiedTextUseCase,
    private val updateCopiedTextUseCase: UpdateCopiedTextUseCase,
    private val deleteCopiedTextUseCase: DeleteCopiedTextUseCase
) : BaseViewModel() {
    val copiedTextFlow: Flow<PagingData<CopiedText>> = getAllCopiedTextsUseCase.flow.cachedIn(viewModelScope)

    fun getCopiedTexts() {
        getAllCopiedTextsUseCase(
            GetAllCopiedTextsUseCase.Params(
                PagingConfig(
                    pageSize = 50,
                    initialLoadSize = 100,
                    maxSize = 200
                )
            )
        )
    }

    fun addCopiedText(content: String) {
        viewModelScope.launch {
            addCopiedTextUseCase(
                AddCopiedTextUseCase.Params(
                    CopiedText(
                        id = 0L,
                        text = content,
                        lastUpdated = Date().time
                    )
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong!"))
                    else -> {}
                }
            }
        }
    }

    fun updateCopiedText(
        id: Long,
        content: String,
    ) {
        viewModelScope.launch {
            updateCopiedTextUseCase(
                UpdateCopiedTextUseCase.Params(
                    copiedText = CopiedText(
                        id = id,
                        text = content,
                        lastUpdated = Date().time
                    )
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong!"))
                    else -> {}
                }
            }
        }
    }

    fun deleteCopiedTexts(copiedTexts: List<Long>) {
        viewModelScope.launch {
            deleteCopiedTextUseCase(
                DeleteCopiedTextUseCase.Params(
                    copiedTexts = copiedTexts
                )
            ).collect {
                when (it) {
                    is InvokeStatus.Failure -> sendEvent(UIEvent.ShowToast("Something went wrong!"))
                    else -> {}
                }
            }
        }
    }
}
