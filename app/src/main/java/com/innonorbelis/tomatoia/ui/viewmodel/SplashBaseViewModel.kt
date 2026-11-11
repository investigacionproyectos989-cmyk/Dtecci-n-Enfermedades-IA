package com.innonorbelis.tomatoia.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class SplashBaseViewModel:ViewModel() {
    //errore
    val errorLiveData=MutableLiveData<Throwable>()
    //observer de apagado
    val loadingLiveData=MutableLiveData<Boolean>()
    //funcion del remplazo del viewMOdel
    fun execute(loading:Boolean=true,func:suspend ()->Unit){
        //courrutina hilo secundario
        viewModelScope.launch(Dispatchers.IO){
            try {
                loadingLiveData.postValue(loading)
                func()
                loadingLiveData.postValue(false)

            }catch (ex:Exception){
                errorLiveData.postValue(ex)
                loadingLiveData.postValue(false)
            }

        }

    }
}