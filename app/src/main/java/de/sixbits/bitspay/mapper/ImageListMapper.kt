package de.sixbits.bitspay.mapper

import de.sixbits.bitspay.database.entities.ImageEntity
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

    fun fromImageEntityModel(hit: ImageEntity): ImageListItemModel {
        return ImageListItemModel(
            id = hit.id,
            username = hit.username,
            thumbnail = hit.image,
            tags = hit.tags
        )
    }
}