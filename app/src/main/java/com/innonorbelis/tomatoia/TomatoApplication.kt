package com.innonorbelis.tomatoia

import android.app.Application
import com.innonorbelis.tomatoia.data.local.seeder.DatabaseSeeder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class TomatoApplication : Application(), CoroutineScope by MainScope() {

    @Inject lateinit var seeder: DatabaseSeeder

    override fun onCreate() {
        super.onCreate()

        launch(Dispatchers.IO) {
            seeder.seed()
        }
    }
}


