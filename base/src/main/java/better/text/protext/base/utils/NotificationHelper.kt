package better.text.protext.base.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.DimenRes
import androidx.core.app.NotificationCompat

object NotificationHelper {
    fun createOrUpdateNotification(context: Context, data: NotificationData): Notification {
        createNotificationChannel(
            context = context,
            channelId = data.channelId,
            channelName = data.channelName,
            channelDescription = data.channelDescription,
            importance = data.importance
        )

        val builder = NotificationCompat.Builder(context, data.channelId)
            .setSmallIcon(data.iconId)
            .setContentTitle(data.title)
            .setContentText(data.content)
            .setPriority(data.priority)

        for (action in data.actions) {
            builder.addAction(
                NotificationCompat.Action.Builder(
                    action.actionIcon,
                    action.actionTitle,
                    action.action
                ).build()
            )
        }

        data.clickAction?.let {
            builder.setContentIntent(it)
        }

        return builder.build()
    }

    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        importance: Int
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

data class NotificationData(
    val title: String,
    val content: String,
    @DimenRes val iconId: Int,
    val notificationId: Int,
    val channelId: String,
    val channelName: String,
    val channelDescription: String,
    val actions: List<NotificationAction> = listOf(),
    val clickAction: PendingIntent? = null,
    val importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
    val priority: Int = NotificationCompat.PRIORITY_DEFAULT
)

data class NotificationAction(
    val actionTitle: String,
    @DimenRes val actionIcon: Int,
    val action: PendingIntent
)
