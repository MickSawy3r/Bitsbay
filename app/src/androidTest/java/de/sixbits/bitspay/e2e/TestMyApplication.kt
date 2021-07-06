package de.sixbits.bitspay.e2e

import de.sixbits.bitspay.MyApplication
import de.sixbits.bitspay.e2e.di.DaggerTestAppComponent
import de.sixbits.bitspay.e2e.di.TestAppComponent

class TestMyApplication : MyApplication() {

    @Override
    fun initializeComponent(): TestAppComponent {
        return DaggerTestAppComponent.builder()
            .application(this)
            .build()
    }

}