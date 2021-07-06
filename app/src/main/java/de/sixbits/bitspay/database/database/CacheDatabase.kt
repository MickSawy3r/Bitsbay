package de.sixbits.bitspay.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.sixbits.bitspay.database.converters.DateConverter
import de.sixbits.bitspay.database.dao.CacheDao
import de.sixbits.bitspay.database.entities.ImageEntity

@Database(
    entities = [ImageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class CacheDatabase : RoomDatabase() {
    abstract fun cacheDao(): CacheDao
}