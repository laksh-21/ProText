package better.text.protext.base.baseScreens

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.lifecycle.LifecycleService
import androidx.viewbinding.ViewBinding

abstract class BaseForegroundWindowService<B : ViewBinding>(
    private val notificationChannelId: String,
    private val notificationChannelName: String,
    private val notificationTitle: String,
    private val notificationText: String,
    @DrawableRes private val notificationIcon: Int?,
    private val windowGravity: Int,
    @StyleRes private val windowAnimations: Int,
    private val action: () -> Unit
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

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        _binding = inflater(LayoutInflater.from(this@BaseForegroundWindowService))
        binding.root.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_OUTSIDE -> {
                    close()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.intent = intent
        onCreate(binding)
        return super.onStartCommand(intent, flags, startId)
    }

    protected fun createNotification(): Notification {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                notificationChannelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this@BaseForegroundWindowService, notificationChannelId)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this@BaseForegroundWindowService)
        }
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
        notificationIcon?.let {
            notification.setSmallIcon(it)
        }
        return notification.build()
    }

    protected fun close() {
        windowManager.removeView(binding.root)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            windowManager.removeView(binding.root)
        } catch (e: Exception) {
            Log.d("Protext", "Caught exception: ${e.message}")
        }
        _binding = null
        intent = null
    }
}
