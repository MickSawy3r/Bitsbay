package de.sixbits.bitspay.unit.main.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.sixbits.bitspay.ImageResponseFactory
import de.sixbits.bitspay.main.repository.MainRepository
import de.sixbits.bitspay.main.view_model.SharedViewModel
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

@RunWith(JUnit4::class)
class TestMainViewModel {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testSearchForEmptyResult() {
        val mainRepository = Mockito.mock(MainRepository::class.java)

        Mockito.`when`(mainRepository.searchFor(anyString()))
            .thenReturn(Observable.error(Exception("Error")))
        val mainViewModel = SharedViewModel(mainRepository)

        mainViewModel.searchImagesLiveData.observeForever {
            assert(it.isEmpty())
        }
        mainViewModel.searchFor("query")
    }

    @Test
    fun testSearchForOnResult() {
        val mainRepository = Mockito.mock(MainRepository::class.java)

        Mockito.`when`(mainRepository.searchFor(anyString()))
            .thenReturn(Observable.just(listOf(ImageResponseFactory.getImageListItem())))
        val mainViewModel = SharedViewModel(mainRepository)

        mainViewModel.searchImagesLiveData.observeForever {
            assert(it.isNotEmpty())
        }
        mainViewModel.searchFor("query")
    }

    @Test
    fun testGetCachedImagesWithCache() {
        val mainRepository = Mockito.mock(MainRepository::class.java)
        Mockito.`when`(mainRepository.getCached())
            .thenReturn(Observable.just(listOf(ImageResponseFactory.getImageListItem())))

        val mainViewModel = SharedViewModel(mainRepository)

        mainViewModel.searchImagesLiveData.observeForever {
            assert(it.isNotEmpty())
        }

        mainViewModel.getCachedImages()
    }

    @Test
    fun testGetCachedImagesWithNoCache() {
        val mainRepository = Mockito.mock(MainRepository::class.java)
        Mockito.`when`(mainRepository.getCached())
            .thenReturn(Observable.just(listOf()))

        val mainViewModel = SharedViewModel(mainRepository)

        mainViewModel.searchImagesLiveData.observeForever {
            assert(it.isEmpty())
        }

        mainViewModel.getCachedImages()
    }
}