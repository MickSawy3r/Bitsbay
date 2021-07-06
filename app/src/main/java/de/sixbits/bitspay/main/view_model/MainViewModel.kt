package de.sixbits.bitspay.main.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.sixbits.bitspay.EspressoIdlingResource
import de.sixbits.bitspay.main.repository.MainRepository
import de.sixbits.bitspay.network.model.ImageListItemModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val searchImagesLiveData = MutableLiveData<List<ImageListItemModel>>()
    val pagerLiveData = MutableLiveData<List<ImageListItemModel>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()

    private var activeSearchQuery = ""
    private var activePage = 1

    fun searchFor(query: String) {
        activeSearchQuery = query
        activePage = 1
        loadingLiveData.postValue(true)

        EspressoIdlingResource.increment()
        mainRepository.searchFor(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                EspressoIdlingResource.decrement()
                searchImagesLiveData.postValue(it)
                loadingLiveData.postValue(false)
            }, {
                searchImagesLiveData.postValue(listOf())
                errorLiveData.postValue(it.message)
            })
    }

    fun requestMoreImage() {
        activePage++
        EspressoIdlingResource.increment()
        mainRepository.requestSearchPage(activeSearchQuery, activePage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                EspressoIdlingResource.decrement()
                pagerLiveData.postValue(it)
            }, {
                pagerLiveData.postValue(listOf())
            })
    }
}
