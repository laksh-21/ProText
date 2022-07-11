package better.text.protext

import better.text.protext.base.baseScreens.BaseViewModel
import better.text.protext.preferecnes.DatastoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    datastoreManager: DatastoreManager
) : BaseViewModel() {
    val isOnBoardingDoneFlow = datastoreManager.isOnBoardingDone
}
