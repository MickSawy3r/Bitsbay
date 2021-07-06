package de.sixbits.bitspay.main.broadcast_reciever

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.AndroidEntryPoint
import de.sixbits.bitspay.main.repository.MainRepository
import de.sixbits.bitspay.network.model.ImageListItemModel
import de.sixbits.bitspay.preferences.SharedPreferencesHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


@AndroidEntryPoint
class FeedCleanerBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var mainRepository: MainRepository

    @Inject
    lateinit var preferencesHelper: SharedPreferencesHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        preferencesHelper.setNotificationsScheduled(false)
        mainRepository.getSaved()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.size > 10 && context != null && intent != null) {
                    removeList(context, intent, it.subList(10, it.size))
                }
            }
    }

    private fun removeList(context: Context, intent: Intent, images: List<ImageListItemModel>) {
        mainRepository.removeList(images)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification: Notification? =
                    intent.getParcelableExtra(NOTIFICATION)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val notificationChannel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "NOTIFICATION_CHANNEL_NAME",
                        importance
                    )
                    notificationManager.createNotificationChannel(notificationChannel)
                }
                val id: Int = intent.getIntExtra(NOTIFICATION_ID, 0)
                notificationManager.notify(id, notification)
            }
    }

    companion object {
        const val NOTIFICATION_ID = "notification-id"
        const val NOTIFICATION = "notification"
        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val DEFAULT_NOTIFICATION_CHANNEL_ID = "default"
    }
}
