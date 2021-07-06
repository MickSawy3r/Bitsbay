package de.sixbits.bitspay.main.repository

import android.util.Log
import de.sixbits.bitspay.database.dao.CacheDao
import de.sixbits.bitspay.database.entities.ImageEntity
import de.sixbits.bitspay.mapper.ImageEntityMapper
import de.sixbits.bitspay.mapper.ImageListMapper
import de.sixbits.bitspay.network.manager.PixabayManager
import de.sixbits.bitspay.network.model.ImageListItemModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "MainRepository"

open class MainRepository @Inject constructor(
    private val pixabayManager: PixabayManager,
    private val cacheDao: CacheDao
) {

    fun getSaved(): Observable<List<ImageListItemModel>> {
        return this.cacheDao.getAll()
            .map { cached ->
                cached.map { i ->
                    ImageListMapper.fromImageEntityModel(i)
                }
            }
    }

    fun trashImage(image: ImageListItemModel): Observable<Unit> {
        return this.cacheDao.getById(image.id)
            .map {
                val newItem = ImageEntity(
                    id = it.id,
                    image = it.image,
                    username = it.username,
                    tags = it.tags,
                    createdAt = it.createdAt,
                    trashed = true
                )
                this.cacheDao.update(newItem)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe()
            }
    }

    fun getTrashed(): Observable<List<ImageListItemModel>> {
        return this.cacheDao.getTrashed()
            .map { cached ->
                cached.map { i ->
                    ImageListMapper.fromImageEntityModel(i)
                }
            }
    }

    fun searchFor(query: String): Observable<List<ImageListItemModel>> {
        return this.cacheDao.search("$query%")
            .map { imageList ->
                imageList.map { imageItem ->
                    ImageListMapper.fromImageEntityModel(imageItem)
                }
            }
    }

    fun insertItem(image: ImageListItemModel): Completable {
        return this.cacheDao.insert(image = ImageEntityMapper.fromImageListItem(image))
    }

    fun insertItemList(images: List<ImageListItemModel>): Completable {
        return this.cacheDao.insertList(image = images.map {
            ImageEntityMapper.fromImageListItem(it)
        })
    }

    fun delete(image: ImageListItemModel): Completable {
        return this.cacheDao.delete(image = ImageEntityMapper.fromImageListItem(image))
    }

    fun getDummyContent(): Observable<List<ImageListItemModel>> {
        return pixabayManager.getSearchResult("food")

    }
}