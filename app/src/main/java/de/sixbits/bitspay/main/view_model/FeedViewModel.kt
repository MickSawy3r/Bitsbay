package de.sixbits.bitspay.main.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.sixbits.bitspay.EspressoIdlingResource
import de.sixbits.bitspay.main.repository.MainRepository
import de.sixbits.bitspay.network.model.ImageListItemModel
import de.sixbits.bitspay.preferences.SharedPreferencesHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "FeedViewModel"

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {
    val loadingLiveData = MutableLiveData<Boolean>()
    val searchImagesLiveData = MutableLiveData<List<ImageListItemModel>>()
    val errorLiveData = MutableLiveData<String>()

    fun init() {
        if (!sharedPreferencesHelper.getInited()) {
            Log.d(TAG, "init: ")
            mainRepository.getDummyContent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        Log.d(TAG, "init: Saving Data")
                        mainRepository.insertItemList(it)
                            .observeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .subscribe({
                                getAll()
                            }, { err ->
                                Log.d(TAG, "init: error ${err.message}")
                            })
                    }, {
                        errorLiveData.postValue(it.message)
                    }
                )
        } else {
            getAll()
        }
    }

    fun getAll() {
        Log.d(TAG, "getAll: ")
        loadingLiveData.postValue(true)
        mainRepository.getTrashed()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    searchImagesLiveData.postValue(it)
                    loadingLiveData.postValue(false)
                },
                {
                    errorLiveData.postValue(it.message)
                }
            )
    }

    fun searchFor(query: String) {
        Log.d(TAG, "searchFor: ")
        loadingLiveData.postValue(true)

        EspressoIdlingResource.increment()
        mainRepository.searchFor("%${query}%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    EspressoIdlingResource.decrement()
                    searchImagesLiveData.postValue(it)
                    loadingLiveData.postValue(false)
                }, {
                    searchImagesLiveData.postValue(listOf())
                    errorLiveData.postValue(it.message)
                }
            )
    }
}