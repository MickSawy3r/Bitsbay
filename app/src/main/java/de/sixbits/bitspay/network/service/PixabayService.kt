package de.sixbits.bitspay.network.service

import de.sixbits.bitspay.network.response.PixabaySearchResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PixabayService {
    @GET("/api/")
    fun getSearchResult(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Observable<PixabaySearchResponse>

    @GET("/api/")
    fun getImageDetails(
        @Query("id") id: Int,
        @Query("key") apiKey: String
    ): Observable<PixabaySearchResponse>
}
