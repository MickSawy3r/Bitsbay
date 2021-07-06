package de.sixbits.bitspay.unit.network.service

import de.sixbits.bitspay.BuildConfig
import de.sixbits.bitspay.network.service.PixabayService
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(JUnit4::class)
class TestPixabayService {

    lateinit var webServer: MockWebServer

    @Before
    fun setup() {
        // Prepairing RX
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        // Mocking the server
        webServer = MockWebServer()
        webServer.start()
    }

    @Test
    fun testGetSearchResult() {
        val response = MockResponse().setBody(
            "{\n" +
                    "\t\"total\": 4692,\n" +
                    "\t\"totalHits\": 500,\n" +
                    "\t\"hits\": [\n" +
                    "\t    {\n" +
                    "\t        \"id\": 195893,\n" +
                    "\t        \"pageURL\": \"https://pixabay.com/en/blossom-bloom-flower-195893/\",\n" +
                    "\t        \"type\": \"photo\",\n" +
                    "\t        \"tags\": \"blossom, bloom, flower\",\n" +
                    "\t        \"previewURL\": \"https://cdn.pixabay.com/photo/2013/10/15/09/12/flower-195893_150.jpg\",\n" +
                    "\t        \"previewWidth\": 150,\n" +
                    "\t        \"previewHeight\": 84,\n" +
                    "\t        \"webformatURL\": \"https://pixabay.com/get/35bbf209e13e39d2_640.jpg\",\n" +
                    "\t        \"webformatWidth\": 640,\n" +
                    "\t        \"webformatHeight\": 360,\n" +
                    "\t        \"largeImageURL\": \"https://pixabay.com/get/ed6a99fd0a76647_1280.jpg\",\n" +
                    "\t        \"fullHDURL\": \"https://pixabay.com/get/ed6a9369fd0a76647_1920.jpg\",\n" +
                    "\t        \"imageURL\": \"https://pixabay.com/get/ed6a9364a9fd0a76647.jpg\",\n" +
                    "\t        \"imageWidth\": 4000,\n" +
                    "\t        \"imageHeight\": 2250,\n" +
                    "\t        \"imageSize\": 4731420,\n" +
                    "\t        \"views\": 7671,\n" +
                    "\t        \"downloads\": 6439,\n" +
                    "\t        \"favorites\": 1,\n" +
                    "\t        \"likes\": 5,\n" +
                    "\t        \"comments\": 2,\n" +
                    "\t        \"user_id\": 48777,\n" +
                    "\t        \"user\": \"Josch13\",\n" +
                    "\t        \"userImageURL\": \"https://cdn.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg\"\n" +
                    "\t    }\n" +
                    "\t]\n" +
                    "}"
        )
        webServer.enqueue(response)

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(webServer.url("localhost/"))
            .build()

        val pixabayService = retrofit.create(PixabayService::class.java)

        pixabayService
            .getSearchResult("fruits", BuildConfig.PIXABAY_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                assert(it.total > 0)
            }, {
                print(it.message)
                assert(false)
            })
    }

    @After
    fun cleanUp() {
        webServer.shutdown()
    }
}