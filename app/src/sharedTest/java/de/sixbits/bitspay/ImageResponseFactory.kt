package de.sixbits.bitspay

import de.sixbits.bitspay.network.model.ImageListItemModel

object ImageResponseFactory {
    fun getImageListItem(): ImageListItemModel {
        return ImageListItemModel(
            thumbnail = "https://cdn.pixabay.com/photo/2013/10/15/09/12/flower-195893_150.jpg",
            username = "Mohammad",
            tags = "tag1, tag2, tag3",
            id = 1
        )
    }
}