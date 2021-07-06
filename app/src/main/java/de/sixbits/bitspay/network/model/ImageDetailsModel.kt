package de.sixbits.bitspay.network.model

data class ImageDetailsModel constructor(
    val id: Int,
    val image: String,
    val username: String,
    val tags: String,
    val likes: Int,
    val favorites: Int,
    val comments: Int
)