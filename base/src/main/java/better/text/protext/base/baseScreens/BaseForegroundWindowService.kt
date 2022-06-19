package better.text.protext.base.baseScreens

import android.app.Notification
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.lifecycle.LifecycleService
import androidx.viewbinding.ViewBinding
import better.text.protext.base.utils.NotificationData
import better.text.protext.base.utils.NotificationHelper

abstract class BaseForegroundWindowService<B : ViewBinding>(
    private val windowGravity: Int,
    @StyleRes private val windowAnimations: Int,
) : LifecycleService() {

    private val windowType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        @Suppress("DEPRECATION")
        WindowManager.LayoutParams.TYPE_PHONE
    }

    private val windowFlags = (
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
            WindowManager.LayoutParams.FLAG_DIM_BEHIND or
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        )

    protected val layoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        windowType,
        windowFlags,
        PixelFormat.TRANSLUCENT
    ).also {
        it.windowAnimations = windowAnimations
        it.gravity = windowGravity
        it.dimAmount = 0.5f
    }

    abstract val windowManager: WindowManager
    private var _binding: B? = null
    protected val binding: B get() = _binding!!
    protected var intent: Intent? = null

    protected abstract fun inflater(inflater: LayoutInflater): B
    abstract fun onCreate(binding: B)
    abstract fun onStartCommand(intent: Intent?)

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        _binding = inflater(LayoutInflater.from(this@BaseForegroundWindowService))
        onCreate(binding)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.intent = intent
        onStartCommand(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    protected fun createNotification(notificationData: NotificationData): Notification {
        return NotificationHelper.createOrUpdateNotification(this, notificationData)
    }

    protected fun close() {
        try {
            windowManager.removeView(binding.root)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            windowManager.removeView(binding.root)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _binding = null
        intent = null
    }
}
