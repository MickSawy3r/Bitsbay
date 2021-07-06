package de.sixbits.bitspay

import de.sixbits.bitspay.network.response.PixabaySearchResponse

object PixabayResponseFactory {

    fun getResponse(): PixabaySearchResponse {
        return PixabaySearchResponse(
            total = 1,
            totalHits = 1,
            hits = listOf(
                PixabaySearchResponse.Hit(
                    previewURL = "https://cdn.pixabay.com/photo/2013/10/15/09/12/flower-195893_150.jpg",
                    user = "Mohammad",
                    tags = "tag1, tag2, tag3",
                    comments = 0,
                    downloads = 0,
                    id = 1,
                    largeImageURL = "",
                    likes = 5,
                    pageURL = "",
                    userImageURL = "",
                    webformatURL = "",
                    favorites = 1,
                    imageHeight = 12,
                    imageSize = 1,
                    imageWidth = 1,
                    webformatHeight = 1,
                    webformatWidth = 1,
                    userId = 1,
                    previewHeight = 1,
                    previewWidth = 1,
                    type = "1",
                    views = 1
                )
            )
        )
    }

    fun getZeroResponse(): PixabaySearchResponse {
        return PixabaySearchResponse(
            hits = listOf(),
            total = 0,
            totalHits = 0
        )
    }
}