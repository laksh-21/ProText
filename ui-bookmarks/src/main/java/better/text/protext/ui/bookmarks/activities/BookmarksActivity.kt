package better.text.protext.ui.bookmarks.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import better.text.protext.base.baseScreens.BaseActivity
import better.text.protext.base.utils.PermissionHelper
import better.text.protext.base.utils.Validators
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.databinding.ActivityBookmarksBinding
import better.text.protext.ui.bookmarks.services.BookmarkService
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookmarksActivity : BaseActivity<ActivityBookmarksBinding>() {

    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?, binding: ActivityBookmarksBinding) {
        checkValidUrl()
        if (!isFinishing) {
            setupPermissionHelper()
            checkForOverlayPermission()
            setContentView(R.layout.activity_bookmarks)
        }
    }

    private fun checkValidUrl() {
        if (intent.action != Intent.ACTION_SEND && intent.type != "text/plain") {
            finish()
            return
        }
        val url = intent.getStringExtra(Intent.EXTRA_TEXT)
        url?.let {
            if (!Validators.validateUrl(it)) finish()
        }
    }

    private fun setupPermissionHelper() {
        permissionHelper = PermissionHelper(this, activityResultRegistry) {
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
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        )
    }

    private fun launchService() {
        val serviceIntent = Intent(this, BookmarkService::class.java).apply {
            putExtra(BookmarkService.URL_EXTRA_NAME, intent.getStringExtra(Intent.EXTRA_TEXT))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        finish()
    }

    override fun getViewBinding(): ActivityBookmarksBinding {
        return ActivityBookmarksBinding.inflate(layoutInflater)
    }
}
