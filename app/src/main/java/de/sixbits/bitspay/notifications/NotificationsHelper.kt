package de.sixbits.bitspay.notifications

import android.app.AlarmManager
import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import de.sixbits.bitspay.R
import de.sixbits.bitspay.config.Consts.NOTIFICATION_DELAY
import de.sixbits.bitspay.main.broadcast_reciever.FeedCleanerBroadcastReceiver
import javax.inject.Inject

private const val TAG = "NotificationsHelper"

class NotificationsHelper @Inject constructor(val application: Application) {

    fun scheduleNotification() {
        Log.d(TAG, "scheduleNotification: ")
        val notification = getNotification()

        val delay = NOTIFICATION_DELAY
        val notificationIntent = Intent(application, FeedCleanerBroadcastReceiver::class.java)
        notificationIntent.putExtra(FeedCleanerBroadcastReceiver.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(FeedCleanerBroadcastReceiver.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            application,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val futureInMillis: Long = SystemClock.elapsedRealtime() + delay
        val alarmManager = (application.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
    }

    private fun getNotification(): Notification {
        val content = "Cleaning Feed"
        val builder = NotificationCompat.Builder(application,
            FeedCleanerBroadcastReceiver.DEFAULT_NOTIFICATION_CHANNEL_ID
        )
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_feed)
        builder.setAutoCancel(true)
        builder.setChannelId(FeedCleanerBroadcastReceiver.NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }
}