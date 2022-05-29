package better.text.protext.base.baseScreens

import android.widget.Toast
import androidx.navigation.NavDirections

sealed class UIEvent {
    data class Navigate(val action: NavDirections) : UIEvent()
    object GoBack : UIEvent()
    object ClearSelections : UIEvent()
    data class ShowToast(val message: String, val duration: Int = Toast.LENGTH_SHORT) : UIEvent()
    data class ShowSnackBar(val message: String, val duration: Int = Toast.LENGTH_SHORT) : UIEvent()
}
