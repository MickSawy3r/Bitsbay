package de.sixbits.bitspay.main.view_model

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.sixbits.bitspay.main.repository.MainRepository
import de.sixbits.bitspay.main.ui.fragments.FeedFragment
import de.sixbits.bitspay.main.ui.fragments.TrashFragment
import de.sixbits.bitspay.network.model.ImageListItemModel
import de.sixbits.bitspay.notifications.NotificationsHelper
import de.sixbits.bitspay.preferences.SharedPreferencesHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val preferencesHelper: SharedPreferencesHelper,
    private val notificationsHelper: NotificationsHelper
) :
    ViewModel() {

    val activePageLiveData = MutableLiveData<ActiveFragment>(ActiveFragment.FEED)
    val changeLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()

    fun showFeed() {
        activePageLiveData.postValue(ActiveFragment.FEED)
    }

    fun showTrash() {
        activePageLiveData.postValue(ActiveFragment.TRASH)
    }

    fun saveImage(image: Uri) {
        mainRepository.insertItem(
            ImageListItemModel(
                id = Random.nextInt(),
                thumbnail = image.toString(),
                username = "Me",
                tags = "Mine!"
            )
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    changeLiveData.postValue(true)
                },
                {
                    errorLiveData.postValue(it.message)
                }
            )
    }

    fun scheduleNotificationIfNotExists() {
        if (!preferencesHelper.getNotificationsScheduled()) {
            preferencesHelper.setNotificationsScheduled(true)
            notificationsHelper.scheduleNotification()
        }
    }

    enum class ActiveFragment {
        FEED,
        TRASH
    }
}
