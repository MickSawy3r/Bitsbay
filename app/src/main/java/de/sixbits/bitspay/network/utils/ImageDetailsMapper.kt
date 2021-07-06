package de.sixbits.bitspay.network.utils

import de.sixbits.bitspay.network.model.ImageDetailsModel
import de.sixbits.bitspay.network.response.PixabaySearchResponse

object ImageDetailsMapper {
    fun fromImageListItemModel(it: PixabaySearchResponse.Hit): ImageDetailsModel {
        return ImageDetailsModel(
            id = it.id,
            image = it.largeImageURL,
            username = it.user,
            tags = it.tags,
            comments = it.comments,
            favorites = it.favorites,
            likes = it.likes
        )
    }
}