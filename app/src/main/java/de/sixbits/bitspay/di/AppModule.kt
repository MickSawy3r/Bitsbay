package de.sixbits.bitspay.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.sixbits.bitspay.config.Consts
import de.sixbits.bitspay.database.dao.CacheDao
import de.sixbits.bitspay.database.database.CacheDatabase
import de.sixbits.bitspay.network.manager.PixabayManager
import de.sixbits.bitspay.network.service.PixabayService
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
open class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CacheDatabase {
        return Room.databaseBuilder(
            appContext,
            CacheDatabase::class.java,
            "RssReader"
        ).build()
    }

    @Provides
    fun provideChannelDao(appDatabase: CacheDatabase): CacheDao {
        return appDatabase.cacheDao()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Consts.API_ROOT)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        application: Application
    ): RequestManager {
        return Glide.with(application)
    }


    @Singleton
    @Provides
    fun providePixabayManager(pixabayService: PixabayService): PixabayManager {
        return PixabayManager(pixabayService)
    }

    @Singleton
    @Provides
    fun providePixabayService(retrofit: Retrofit): PixabayService {
        return retrofit.create(PixabayService::class.java)
    }
}