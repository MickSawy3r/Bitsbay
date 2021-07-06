package de.sixbits.bitspay.network.utils

import de.sixbits.bitspay.network.model.ImageListItemModel
import de.sixbits.bitspay.network.response.PixabaySearchResponse

object ImageListMapper {
    fun fromImageListItemModel(hit: PixabaySearchResponse.Hit): ImageListItemModel {
        return ImageListItemModel(
            id = hit.id,
            username = hit.user,
            thumbnail = hit.previewURL,
            tags = hit.tags
        )
    }
}