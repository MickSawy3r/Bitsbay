package de.sixbits.bitspay.mapper

import de.sixbits.bitspay.database.entities.ImageEntity
import de.sixbits.bitspay.network.model.ImageListItemModel
import java.util.*

object ImageEntityMapper {
    fun fromImageListItem(model: ImageListItemModel): ImageEntity {
        return ImageEntity(
            id = model.id,
            image = model.thumbnail,
            username = model.username,
            tags = model.tags,
            createdAt = Calendar.getInstance().time
        )
    }
}