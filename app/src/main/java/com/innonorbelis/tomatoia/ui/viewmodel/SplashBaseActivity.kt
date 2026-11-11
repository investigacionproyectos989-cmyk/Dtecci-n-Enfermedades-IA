package com.innonorbelis.tomatoia.ui.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.log

//clases abstractas
abstract class SplashBaseActivity: AppCompatActivity(){
    // imlementarlas de clases que hereden de esta clase
    abstract  fun getBinding()//obtener el binding
    abstract  fun setView()//uilizar para crear dentro el oncreate
    abstract  fun observerVViewModel()//observador del view model
    abstract  fun getVienModel(): SplashViewModel?//va a pasar el view model para que la base activiy lo pueda manejar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding()
        setView()
        observerBaseActivity()
    }
    private  fun observerBaseActivity(){
        this.observerVViewModel()
        // Observar errores emitidos por el ViewModel
        this.getVienModel()?.errorLiveData?.observe(
            this){error->
            error?.message?.let { message->
                Log.d("error",message)
            }?: Log.d("error","Error desconocido")
        }
    }
}