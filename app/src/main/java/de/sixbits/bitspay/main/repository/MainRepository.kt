package de.sixbits.bitspay.main.repository

import de.sixbits.bitspay.network.manager.PixabayManager
import de.sixbits.bitspay.network.model.ImageListItemModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

open class MainRepository @Inject constructor(
    private val pixabayManager: PixabayManager
) {

    fun searchFor(query: String): Observable<List<ImageListItemModel>> {
        return this.pixabayManager.getSearchResult(query)
    }

    fun requestSearchPage(query: String, pageNumber: Int): Observable<List<ImageListItemModel>> {
        return this.pixabayManager.getSearchResultPage(query, pageNumber)
    }
}