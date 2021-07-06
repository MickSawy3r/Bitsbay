package de.sixbits.bitspay

import de.sixbits.bitspay.database.entities.ImageEntity
import java.util.*

object ImageEntityFactory {

    fun getImageItem1(): ImageEntity {
        return ImageEntity(
            id = 1,
            image = "https://google.com/",
            username = "Mick",
            tags = "1, 2",
            likes = 1,
            favorites = 1,
            comments = 1,
            createdAt = Calendar.getInstance().time
        )
    }

    fun getImageItem2(): ImageEntity {
        return ImageEntity(
            id = 2,
            image = "https://google.com/",
            username = "Mick 2",
            tags = "1, 2, 3",
            likes = 2,
            favorites = 2,
            comments = 2,
            createdAt = Calendar.getInstance().time
        )
    }
}