package com.innonorbelis.tomatoia.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.innonorbelis.tomatoia.R
import com.innonorbelis.tomatoia.databinding.ActivityDialogOpcionSiembraBinding
import com.innonorbelis.tomatoia.databinding.ActivityRegistroSiembraBinding

class DialogOpcionSiembraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDialogOpcionSiembraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDialogOpcionSiembraBinding.inflate(layoutInflater)
        setContentView(binding.root)

       //  POPUP
        window.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        binding.nuevaSiembra.setOnClickListener { activityRegistrarSiembra() }
        binding.btnCerrar.setOnClickListener{finish()}
    }
    private fun activityRegistrarSiembra() {
        val intent = Intent(this, RegistroSiembraActivity::class.java)
        startActivity(intent)
        finish() // cierra el popup
    }
}