package better.text.protext.base.utils

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PermissionHelper(
    private val registry: ActivityResultRegistry,
    private val onFinish: (Boolean) -> Unit
) : DefaultLifecycleObserver {
    lateinit var requestPermission: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        requestPermission = registry.register("key", owner, ActivityResultContracts.RequestPermission()) {
            onFinish(it)
        }
    }

    fun requestPermission(permission: String) {
        requestPermission.launch(permission)
    }
}
