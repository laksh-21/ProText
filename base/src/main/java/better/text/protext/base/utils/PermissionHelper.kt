package better.text.protext.base.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PermissionHelper(
    private val context: Context,
    private val registry: ActivityResultRegistry,
    private val onFinish: (Boolean) -> Unit
) : DefaultLifecycleObserver {
    private lateinit var requestPermission: ActivityResultLauncher<String>
    lateinit var requestOverLayPermission: ActivityResultLauncher<Intent>

    override fun onCreate(owner: LifecycleOwner) {
        Log.d("Protext", "created")
        requestPermission = registry.register("permission-key", owner, ActivityResultContracts.RequestPermission()) {
            onFinish(it)
        }
        requestOverLayPermission = registry.register(
            "system-permission-key",
            owner,
            ActivityResultContracts.StartActivityForResult()
        ) {
            Log.d("PermissionHelper", "$it")
            if (Settings.canDrawOverlays(context)) {
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
        requestOverLayPermission.launch(intent)
    }
}
