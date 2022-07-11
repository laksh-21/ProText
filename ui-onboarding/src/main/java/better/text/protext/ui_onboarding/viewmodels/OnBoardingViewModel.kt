package better.text.protext.ui_onboarding.viewmodels // ktlint-disable package-name

import androidx.lifecycle.viewModelScope
import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.preferecnes.DatastoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val datastoreManager: DatastoreManager
) : BaseViewModel() {
    fun updateOnBoardingDone() {
        viewModelScope.launch {
            datastoreManager.updateOnBoardingDone()
        }
    }
}
