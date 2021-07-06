package de.sixbits.bitspay.unit.main.repository

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.sixbits.bitspay.ImageResponseFactory
import de.sixbits.bitspay.database.dao.CacheDao
import de.sixbits.bitspay.mapper.ImageEntityMapper
import de.sixbits.bitspay.network.manager.PixabayManager
import de.sixbits.bitspay.network.utils.NetworkUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.kotlin.any


@RunWith(JUnit4::class)
class TestDetailsRepository {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // Prepairing RX
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testGetImageDetails() {
        val cacheDao = Mockito.mock(CacheDao::class.java)
        val pixabayManager = Mockito.mock(PixabayManager::class.java)
        val networkUtils = Mockito.mock(NetworkUtils::class.java)

        // Test when everything is correct
        val detailsItem = ImageResponseFactory.getImageDetails()

        Mockito.`when`(pixabayManager.getImageDetails(anyInt()))
            .thenReturn(Observable.just(detailsItem))
        Mockito.`when`(cacheDao.insert(any()))
            .thenReturn(Completable.complete())
        Mockito.`when`(cacheDao.insert(any()))
            .thenReturn(Completable.complete())
        Mockito.`when`(networkUtils.isInternetAvailable())
            .thenReturn(true)

        var detailsRepository = DetailsRepository(
            cacheDao = cacheDao,
            pixabayManager = pixabayManager,
            application = Mockito.mock(Application::class.java),
            networkUtils = networkUtils
        )

        detailsRepository.getImageDetails(123)
            .test()
            .assertValue { detailsItem.id == it.id }

        // Test when a network error
        Mockito.`when`(pixabayManager.getImageDetails(123))
            .thenReturn(Observable.error(Exception("No Response")))
        Mockito.`when`(cacheDao.insert(ImageEntityMapper.fromImageListItem(detailsItem)))
            .thenReturn(Completable.complete())

        detailsRepository =
            DetailsRepository(
                cacheDao = cacheDao,
                pixabayManager = pixabayManager,
                application = Mockito.mock(Application::class.java),
                networkUtils = networkUtils
            )

        detailsRepository.getImageDetails(123)
            .test()
            .assertError { it.message == "No Response" }
    }
}