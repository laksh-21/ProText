package better.text.protext.ui.bookmarks.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import better.text.protext.base.utils.PermissionHelper
import better.text.protext.base.utils.Validators
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.services.BookmarkService

class BookmarksActivity : AppCompatActivity() {

    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        permissionHelper = PermissionHelper(activityResultRegistry) {
            launchService()
        }
        lifecycle.addObserver(permissionHelper)
    }

    private fun checkForOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            launchService()
        } else {
            permissionHelper.requestPermission(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        }
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
}
