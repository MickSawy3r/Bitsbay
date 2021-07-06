/**
 * Manages the API service, this class provide the operations on the observables before they
 * get subscribed to by the ViewModel layer.
 * Any Mapping, Filtering ... operations on the RxObservables should happen here
 */

package de.sixbits.bitspay.network.manager

import de.sixbits.bitspay.BuildConfig
import de.sixbits.bitspay.mapper.ImageListMapper
import de.sixbits.bitspay.network.model.ImageListItemModel
import de.sixbits.bitspay.network.service.PixabayService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

open class PixabayManager @Inject constructor(private val pixabayService: PixabayService) {

    /**
     * Mapping Search result into consumables by the UI. No over-fetching should happen when it
     * comes to the view layer.
     */
    fun getSearchResult(searchQuery: String): Observable<List<ImageListItemModel>> {
        return getSearchResultPage(searchQuery, 1)
    }

    fun getSearchResultPage(
        searchQuery: String,
        pageNumber: Int
    ): Observable<List<ImageListItemModel>> {
        // The PIXABAY_KEY is provided in the sent email via local.properties file
        // since it should not be leaked. For more info view the README.md
        return pixabayService.getSearchResult(searchQuery, BuildConfig.PIXABAY_KEY, pageNumber)
            .map {
                val result = mutableListOf<ImageListItemModel>()
                it.hits.forEach { hit ->
                    run {
                        result.add(ImageListMapper.fromImageListItemModel(hit))
                    }
                }
                return@map result
            }
    }
}
