package de.sixbits.bitspay.e2e.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import de.sixbits.bitspay.database.DatabaseComponent
import de.sixbits.bitspay.e2e.TestMyApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestAppModule::class,
        ViewModelModule::class
    ]
)
interface TestAppComponent : AndroidInjector<TestMyApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): TestAppComponent
    }

    fun networkComponent(): NetworkComponent.Factory
    fun mainComponent(): MainComponent.Factory
    fun databaseComponent(): DatabaseComponent.Factory
}



