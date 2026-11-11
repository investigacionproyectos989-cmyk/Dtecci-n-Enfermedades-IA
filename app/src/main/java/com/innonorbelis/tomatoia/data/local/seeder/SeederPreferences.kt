package com.innonorbelis.tomatoia.data.local.seeder

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SeederPreferences @Inject constructor(
    @ApplicationContext
    private val context: Context

) {

    private val prefs = context.getSharedPreferences(
        "seeder_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_VERSION = "seed_version"
    }

    fun getVersion(): Int {
        return prefs.getInt(KEY_VERSION, 0)
    }

    fun saveVersion(version: Int) {
        prefs.edit().putInt(KEY_VERSION, version).apply()
    }
}