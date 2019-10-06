package com.tombecker.stackoverflowquiz.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.tombecker.stackoverflowquiz.util.AppConstants.Companion.LIST_CACHE_TIME_KEY

class SharedPrefsHelper {
    companion object {

        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharedPrefsHelper? = null

        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPrefsHelper = instance ?: synchronized(LOCK) {
            instance ?: buildSharedPrefs(context).also {
                instance = it
            }
        }

        private fun buildSharedPrefs(context: Context): SharedPrefsHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPrefsHelper()
        }
    }

    fun getCachedTime() = prefs?.getLong(LIST_CACHE_TIME_KEY, 0)

    fun setCachedTime(time: Long) {
        prefs?.edit(commit = true) {
            putLong(LIST_CACHE_TIME_KEY, time)
        }
    }
}