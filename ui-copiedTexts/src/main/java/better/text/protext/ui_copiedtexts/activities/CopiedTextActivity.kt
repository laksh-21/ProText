package better.text.protext.ui_copiedtexts.activities // ktlint-disable package-name

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import better.text.protext.base.baseScreens.BaseActivity
import better.text.protext.base.utils.PermissionHelper
import better.text.protext.ui_copiedtexts.R
import better.text.protext.ui_copiedtexts.databinding.ActivityCopiedTextBinding
import better.text.protext.ui_copiedtexts.services.CopiedTextService
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CopiedTextActivity : BaseActivity<ActivityCopiedTextBinding>() {

    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?, binding: ActivityCopiedTextBinding) {
        setupPermissionHelper()
        checkForOverlayPermission()
    }

    private fun setupPermissionHelper() {
        permissionHelper = PermissionHelper(activityResultRegistry) {
            if (it) launchService()
            else {
                Toast.makeText(this, "Could not get required permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        lifecycle.addObserver(permissionHelper)
        permissionHelper.onCreate(this)
    }

    private fun checkForOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            launchService()
        } else {
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.no_permission))
            .setMessage(getString(R.string.grant_permission))
            .setNegativeButton("Reject") { _, _ ->
                finish()
            }
            .setPositiveButton("Allow") { _, _ ->
                requestOverlayPermission()
            }
            .show()
    }

    private fun requestOverlayPermission() {
        permissionHelper.requestSystemPermission(
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package: $packageName"))
        )
    }

    private fun launchService() {
        val serviceIntent = Intent(this, CopiedTextService::class.java).apply {
            action = Intent.ACTION_PROCESS_TEXT
        }
        val copiedText = when (intent.action) {
            Intent.ACTION_SEND -> intent.getStringExtra(Intent.EXTRA_TEXT)
            Intent.ACTION_PROCESS_TEXT -> intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
            else -> ""
        }
        serviceIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, copiedText)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        finish()
    }

    override fun getViewBinding(): ActivityCopiedTextBinding {
        return ActivityCopiedTextBinding.inflate(layoutInflater)
    }
}
