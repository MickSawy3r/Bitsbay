package de.sixbits.bitspay.unit.main.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.sixbits.bitspay.ImageResponseFactory
import de.sixbits.bitspay.main.view_model.DetailsViewModel
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(JUnit4::class)
class TestDetailsViewModel {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testGetImageDetails() {
        val detailsRepository = Mockito.mock(DetailsRepository::class.java)

        Mockito.`when`(detailsRepository.getImageDetails(ArgumentMatchers.anyInt()))
            .thenReturn(Observable.just(ImageResponseFactory.getImageDetails()))
        val detailsViewModel = DetailsViewModel(detailsRepository)

        detailsViewModel.detailsLiveData.observeForever {
            assert(it.username.isNotEmpty())
        }
        detailsViewModel.getImageDetails(123)
    }

    @Test
    fun testGetImageDetailsError() {
        val detailsRepository = Mockito.mock(DetailsRepository::class.java)

        Mockito.`when`(detailsRepository.getImageDetails(ArgumentMatchers.anyInt()))
            .thenReturn(Observable.error(Exception("Bad Exception")))
        val mainViewModel = DetailsViewModel(detailsRepository)

        mainViewModel.detailsLiveData.observeForever {
            assert(it == null)
        }
        mainViewModel.getImageDetails(123)
    }
}