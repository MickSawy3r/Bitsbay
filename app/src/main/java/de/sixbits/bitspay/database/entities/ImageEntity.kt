package de.sixbits.bitspay.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "image_entity")
data class ImageEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "tags") val tags: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "trashed") val trashed: Boolean = false
)
