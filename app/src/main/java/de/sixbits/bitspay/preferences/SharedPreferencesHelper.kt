package de.sixbits.bitspay.preferences

import android.app.Application
import android.content.Context
import javax.inject.Inject

class SharedPreferencesHelper @Inject constructor(private val application: Application) {

    fun setInited(inited: Boolean) {
        val prefs = application.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(INITED, inited)
            .apply()
    }

    fun getInited(): Boolean {
        val prefs = application.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(INITED, false)
    }

    fun setNotificationsScheduled(Scheduled: Boolean) {
        val prefs = application.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(SCHEDULED, Scheduled)
            .apply()
    }

    fun getNotificationsScheduled(): Boolean {
        val prefs = application.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(SCHEDULED, false)
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "GLOBAL"
        private const val INITED = "inited"
        private const val SCHEDULED = "inited"
    }
}