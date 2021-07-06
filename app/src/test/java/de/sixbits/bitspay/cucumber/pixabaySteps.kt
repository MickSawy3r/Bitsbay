package de.sixbits.bitspay.cucumber

import de.sixbits.bitspay.config.Consts
import de.sixbits.bitspay.network.manager.PixabayManager
import de.sixbits.bitspay.network.model.ImageDetailsModel
import de.sixbits.bitspay.network.model.ImageListItemModel
import de.sixbits.bitspay.network.service.PixabayService
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.cucumber.java8.En
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(Cucumber::class)
@CucumberOptions(
    plugin = [
        "pretty"
    ],
    features = ["src/test/assets/features"]
)
class pixabaySteps : En {
    init {
        Given("I have an access to Pixabay API") {
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

            val retrofit = Retrofit
                .Builder()
                .baseUrl(Consts.API_ROOT)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val pixabayService = retrofit.create(PixabayService::class.java)
            pixabayManager = PixabayManager(pixabayService)
        }
        When("I request a search {string}") { query: String ->
            searchObservable = pixabayManager.getSearchResult(query)
        }
        Then("I get an images list result") {
            searchObservable
                .test()
                .assertValue { it.isNotEmpty() }
        }
        When("I request an image with a valid id {int}") { imageId: Int ->
            detailsObservable = pixabayManager.getImageDetails(imageId)
        }
        Then("I should get an Image from user {string}") { authorName: String ->
            detailsObservable
                .test()
                .assertValue { it.username == authorName }
        }
    }

    companion object {
        lateinit var pixabayManager: PixabayManager
        lateinit var searchObservable: Observable<List<ImageListItemModel>>
        lateinit var detailsObservable: Observable<ImageDetailsModel>
    }
}