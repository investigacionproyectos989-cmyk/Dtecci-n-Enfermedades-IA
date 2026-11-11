package com.innonorbelis.tomatoia.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(): SplashBaseViewModel(){
    val successSplash= MutableLiveData<Boolean>()

    fun loadHome(){
        execute {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    successSplash.postValue(true)

                },4000
            )
        }
    }
}