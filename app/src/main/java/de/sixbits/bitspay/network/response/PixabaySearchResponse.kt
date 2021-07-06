package de.sixbits.bitspay.network.response


import com.squareup.moshi.Json

data class PixabaySearchResponse(
    @Json(name = "hits")
    val hits: List<Hit>,
    @Json(name = "total")
    val total: Int, // 30927
    @Json(name = "totalHits")
    val totalHits: Int // 500
) {
    data class Hit(
        @Json(name = "comments")
        val comments: Int, // 154
        @Json(name = "downloads")
        val downloads: Int, // 159055
        @Json(name = "favorites")
        val favorites: Int, // 1145
        @Json(name = "id")
        val id: Int, // 634572
        @Json(name = "imageHeight")
        val imageHeight: Int, // 5017
        @Json(name = "imageSize")
        val imageSize: Int, // 811238
        @Json(name = "imageWidth")
        val imageWidth: Int, // 3345
        @Json(name = "largeImageURL")
        val largeImageURL: String, // https://pixabay.com/get/g2168c0e434f9ce7d793f148a02f0c62b4fc10ba34dbddd298a237e492baee586b81f2f293718152b21d27e43567f3170_1280.jpg
        @Json(name = "likes")
        val likes: Int, // 2233
        @Json(name = "pageURL")
        val pageURL: String, // https://pixabay.com/photos/apples-fruit-red-juicy-ripe-634572/
        @Json(name = "previewHeight")
        val previewHeight: Int, // 150
        @Json(name = "previewURL")
        val previewURL: String, // https://cdn.pixabay.com/photo/2015/02/13/00/43/apples-634572_150.jpg
        @Json(name = "previewWidth")
        val previewWidth: Int, // 100
        @Json(name = "tags")
        val tags: String, // apples, fruit, red
        @Json(name = "type")
        val type: String, // photo
        @Json(name = "user")
        val user: String, // Desertrose7
        @Json(name = "user_id")
        val userId: Int, // 752536
        @Json(name = "userImageURL")
        val userImageURL: String, // https://cdn.pixabay.com/user/2016/03/14/13-25-18-933_250x250.jpg
        @Json(name = "views")
        val views: Int, // 310045
        @Json(name = "webformatHeight")
        val webformatHeight: Int, // 640
        @Json(name = "webformatURL")
        val webformatURL: String, // https://pixabay.com/get/g829a582dbdee32d1133248d61218747f9a1e38a9301c228d15234284a6035b773a2d49bbcd1a5e2d7c2c5ffb7d40ea7c_640.jpg
        @Json(name = "webformatWidth")
        val webformatWidth: Int // 427
    )
}