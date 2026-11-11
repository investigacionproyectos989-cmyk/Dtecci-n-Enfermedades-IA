package com.innonorbelis.tomatoia.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.innonorbelis.tomatoia.ui.viewmodel.SplashBaseViewModel

abstract class SplashBaseActivity : AppCompatActivity() {
    // imlementarlas de clases que hereden de esta clase
    abstract fun getBinding()//obtener el binding
    abstract fun setView()//uilizar para crear dentro el oncreate
    abstract fun observerViewModel()//observador del view model
    abstract fun getViewModel(): SplashBaseViewModel?//va a pasar el view model para que la base activiy lo pueda manejar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding()
        setView()
        observerBaseActivity()
    }
    //observar los metodos genericos
    private fun observerBaseActivity() {
        this.observerViewModel()
        // Observar errores emitidos por el ViewModel
        this.getViewModel()?.errorLiveData?.observe(this) { error ->
            error?.message?.let { message ->
                Log.d("error", message)
            } ?: Log.d("error", "Error desconocido")
        }
    }

}