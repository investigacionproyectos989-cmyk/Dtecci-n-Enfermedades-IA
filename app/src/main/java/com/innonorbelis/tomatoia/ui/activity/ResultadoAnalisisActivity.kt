package com.innonorbelis.tomatoia.ui.activity

import android.animation.ObjectAnimator
import android.net.Uri
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.innonorbelis.tomatoia.R
import com.innonorbelis.tomatoia.databinding.ActivityResultadoAnalisisBinding
import com.innonorbelis.tomatoia.ui.adapter.DashboardAdapter
import com.innonorbelis.tomatoia.ui.viewmodel.AnalisisModeloViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultadoAnalisisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultadoAnalisisBinding

    private val viewModel by viewModels<AnalisisModeloViewModel>()

    private val adapter = DashboardAdapter()

    private var backgroundAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityResultadoAnalisisBinding.inflate(layoutInflater)

        setContentView(binding.root)

       // CONFIGURAR RECYCLERVIEW

        configurarRecyclerView()

        // CONFIGURAR TOOLBAR

        configurarToolbar()


        // OBTENER DATOS

        val resultadoModelo =
            intent.getStringExtra(EXTRA_RESULTADO_MODELO)
                ?: ""

        val resultadoTraducido =
            intent.getStringExtra(EXTRA_RESULTADO_TRADUCIDO)
                ?: ""

        val confianza =
            intent.getIntExtra(EXTRA_CONFIANZA, 0)

        val imageUri =
            intent.getStringExtra(EXTRA_IMAGE_URI)

        cargarImagenAnalizada(imageUri)

        // CONFIGURAR APARIENCIA
       aplicarTemaResultado(
            resultadoModelo.ifEmpty {
                resultadoTraducido
            }
        )

       // OBSERVAR VIEWMODEL
      observarViewModel()

      // CARGAR INFORMACIÓN
         if (resultadoModelo.isNotBlank()) {

            viewModel.procesarResultadoIA(
                nombreModelo = resultadoModelo,
                confianza = confianza,
                imagenUri = imageUri
            )
        }
    }

  // RECYCLERVIEW
  private fun configurarRecyclerView() {

        binding.rvDashboard.apply {

            layoutManager =
                LinearLayoutManager(
                    this@ResultadoAnalisisActivity
                )

            adapter =
                this@ResultadoAnalisisActivity.adapter

            setHasFixedSize(false)
        }
    }

   // OBSERVAR VIEWMODEL
       private fun observarViewModel() {

        lifecycleScope.launch {

            viewModel.dashboardItems.collect { items ->

                adapter.submitList(items)
            }
        }
    }

    private fun cargarImagenAnalizada(uriString: String?) {
        if (uriString.isNullOrBlank()) {
            binding.imgImagenAnalizada.setImageResource(R.drawable.img_leaf)
            return
        }

        try {
            binding.imgImagenAnalizada.setImageURI(Uri.parse(uriString))
        } catch (e: Exception) {
            binding.imgImagenAnalizada.setImageResource(R.drawable.img_leaf)
            android.util.Log.e("ResultadoAnalisis", "No se pudo cargar la imagen: $uriString", e)
        }
    }

    // TOOLBAR
    private fun configurarToolbar() {

        binding.toolbar.setNavigationOnClickListener {

            finish()
        }
    }
   // TEMA DEL RESULTADO

    private fun aplicarTemaResultado(resultado: String) {

        val esSana =
            resultado.equals(
                "healthy",
                ignoreCase = true
            ) ||
                    resultado.equals(
                        "sana",
                        ignoreCase = true
                    ) ||
                    resultado.equals(
                        "hoja sana",
                        ignoreCase = true
                    )

        binding.root.setBackgroundResource(

            if (esSana) {

                R.drawable.bg_gradient_green_resultanalisis

            } else {

                R.drawable.bg_gradient_red_resultanalisis
            }
        )

        iniciarAnimacionFondo()
    }

  // ANIMACIÓN DEL FONDO

    private fun iniciarAnimacionFondo() {

        backgroundAnimator?.cancel()

        backgroundAnimator =
            ObjectAnimator.ofFloat(
                binding.root,
                "alpha",
                1f,
                0.95f,
                1f
            ).apply {

                duration = 3000L

                repeatCount =
                    ObjectAnimator.INFINITE

                interpolator =
                    LinearInterpolator()

                start()
            }
    }

    // CICLO DE VIDA

    override fun onDestroy() {

        backgroundAnimator?.cancel()

        backgroundAnimator = null

        super.onDestroy()
    }

   // CONSTANTES
      companion object {

        const val EXTRA_RESULTADO_MODELO =
            "resultado_modelo"

        const val EXTRA_RESULTADO_TRADUCIDO =
            "resultado_traducido"

        const val EXTRA_CONFIANZA =
            "confianza"

        const val EXTRA_IMAGE_URI =
            "image_uri"

        const val EXTRA_MODO_HISTORIAL =
            "modo_historial"
    }
}