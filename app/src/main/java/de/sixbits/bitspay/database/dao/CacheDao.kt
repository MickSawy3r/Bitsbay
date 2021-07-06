package de.sixbits.bitspay.database.dao

import androidx.room.*
import de.sixbits.bitspay.database.entities.ImageEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface CacheDao {
    @Query("SELECT * from image_entity WHERE trashed = :isTrashed")
    fun getAll(isTrashed: Boolean = false): Observable<List<ImageEntity>>

    @Query("SELECT * from image_entity WHERE trashed = :isTrashed")
    fun getTrashed(isTrashed: Boolean = true): Observable<List<ImageEntity>>

    @Query("SELECT * from image_entity where id = :id AND trashed = :isTrashed")
    fun getById(id: Int, isTrashed: Boolean = false): Observable<ImageEntity>

    @Query("SELECT * from image_entity where username = :searchQuery or tags LIKE :searchQuery AND trashed = :isTrashed")
    fun search(searchQuery: String, isTrashed: Boolean = false): Observable<List<ImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: ImageEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(image: List<ImageEntity>): Completable

    @Update
    fun update(image: ImageEntity): Completable

    @Delete()
    fun delete(image: ImageEntity): Completable
}
