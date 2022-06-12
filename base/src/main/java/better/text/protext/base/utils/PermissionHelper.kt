package better.text.protext.base.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
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
    lateinit var requestSystemPermission: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        Log.d("Protext", "created")
        requestPermission = registry.register("permission-key", owner, ActivityResultContracts.RequestPermission()) {
            onFinish(it)
        }
        requestSystemPermission = registry.register(
            "system-permission-key",
            owner,
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                onFinish(true)
            } else {
                onFinish(false)
            }
        }
    }

    fun requestPermission(permission: String) {
        requestPermission.launch(permission)
    }

    fun requestSystemPermission(intent: Intent) {
        requestSystemPermission.launch(intent)
    }
}
