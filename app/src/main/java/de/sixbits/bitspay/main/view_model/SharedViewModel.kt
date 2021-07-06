package de.sixbits.bitspay.main.view_model

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.sixbits.bitspay.EspressoIdlingResource
import de.sixbits.bitspay.main.repository.MainRepository
import de.sixbits.bitspay.main.ui.fragments.FeedFragment
import de.sixbits.bitspay.main.ui.fragments.TrashFragment
import de.sixbits.bitspay.network.model.ImageListItemModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val activePageLiveData = MutableLiveData<Fragment>(FeedFragment())

    fun showFeed() {
        activePageLiveData.postValue(FeedFragment())
    }

    fun showTrash() {
        activePageLiveData.postValue(TrashFragment())
    }
}
