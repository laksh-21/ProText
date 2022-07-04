package better.text.protext.ui_settings.viewmodels

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.base.results.InvokeStatus
import better.text.protext.interactors.bookmarks.GetAllFoldersUseCase
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.preferecnes.DatastoreManager
import better.text.protext.preferecnes.entities.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val datastoreManager: DatastoreManager,
    private val getAllFoldersUseCase: GetAllFoldersUseCase
) : BaseViewModel() {
    private val userSettingsFlow = datastoreManager.userSettingsFlow
    private val bookmarkFolders = MutableStateFlow<List<BookmarkFolder>?>(null)
    private val _settingsStateFlow = MutableStateFlow<SettingsState?>(null)
    val settingsStateFlow: StateFlow<SettingsState?> = _settingsStateFlow

    init {
        userSettingsFlow.combine(bookmarkFolders) { settings: UserSettings, folders: List<BookmarkFolder>? ->
            _settingsStateFlow.emit(
                folders?.let {
                    SettingsState(
                        userSetting = settings,
                        bookmarkFolders = folders
                    )
                }
            )
        }.launchIn(viewModelScope)
    }

    fun getAllBookmarkFolders() {
        viewModelScope.launch {
            getAllFoldersUseCase(
                GetAllFoldersUseCase.Params
            ).collect {
                when (it) {
                    is InvokeStatus.Success -> bookmarkFolders.emit(it.data)
                    else -> {}
                }
            }
        }
    }

    fun<T> updateSetting(key: Preferences.Key<T>, value: T) {
        viewModelScope.launch {
            datastoreManager.updateUserSetting(key, value)
        }
    }
}

data class SettingsState(
    val userSetting: UserSettings,
    val bookmarkFolders: List<BookmarkFolder>
)
