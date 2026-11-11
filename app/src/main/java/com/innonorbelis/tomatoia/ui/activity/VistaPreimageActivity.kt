package com.innonorbelis.tomatoia.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.innonorbelis.tomatoia.databinding.ActivityVistaPreviaImageBinding

class VistaPreimageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVistaPreviaImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVistaPreviaImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uriString = intent.getStringExtra("image_uri")

        uriString?.let { uri ->

            val uriImagen = Uri.parse(uri)
            binding.ivImagediagnostico.setImageURI(uriImagen)

            // cuando el usuario presione
            binding.btnAnalizar.setOnClickListener {
                enviaImageIA(uri)
            }
        }
    }

    private fun enviaImageIA(uri: String){
        val intent = Intent(this, ProcesamientoActivity::class.java)
        intent.putExtra("image_uri", uri)
        startActivity(intent)
    }
}