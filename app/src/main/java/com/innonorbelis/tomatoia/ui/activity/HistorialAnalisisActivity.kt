package com.innonorbelis.tomatoia.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.innonorbelis.tomatoia.R
import com.innonorbelis.tomatoia.ui.view.HistorialFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistorialAnalisisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_analisis)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contenedorHistorial, HistorialFragment())
                .commit()
        }
    }
}
