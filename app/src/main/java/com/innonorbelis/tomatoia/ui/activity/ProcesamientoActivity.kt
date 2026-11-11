package com.innonorbelis.tomatoia.ui.activity

import android.animation.ObjectAnimator
import android.content.Intent

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.innonorbelis.tomatoia.R
import com.innonorbelis.tomatoia.core.util.traducir
import com.innonorbelis.tomatoia.databinding.ActivityProcesamientoBinding
import com.innonorbelis.tomatoia.ui.viewmodel.AnalisisModeloUiState
import com.innonorbelis.tomatoia.ui.viewmodel.AnalisisModeloViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProcesamientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProcesamientoBinding
    private var scannerAnimator: ObjectAnimator? = null
    private var procesoJob: Job? = null

    private val viewModel by viewModels<AnalisisModeloViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcesamientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observarEstado()

        val uriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        if (uriString.isNullOrBlank()) {
            mostrarError("No se recibió la imagen para analizar.")
        } else {
            procesarImagen(Uri.parse(uriString))
        }
    }

    private fun procesarImagen(uri: Uri) {
        lifecycleScope.launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(contentResolver, uri)
                        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                            decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE)
                            decoder.isMutableRequired = false
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    }
                }

                binding.imgPreview.setImageBitmap(bitmap)
                mostrarEstadosDinamicos()
                viewModel.analizarImagen(bitmap)
            } catch (e: Exception) {
                Log.e(TAG, "Error cargando imagen", e)
                mostrarError("No fue posible cargar la imagen.")
            }
        }
    }

    private fun mostrarEstadosDinamicos() {
        procesoJob?.cancel()

        procesoJob = lifecycleScope.launch {
            cambiarEstado(
                "Preparando imagen...",
                "Optimizando la fotografía para el análisis."
            )

            delay(2500)

            cambiarEstado(
                "Procesando imagen...",
                "La inteligencia artificial está examinando la fotografía."
            )

            delay(3500)

            cambiarEstado(
                "Analizando patrones...",
                "Identificando posibles síntomas de enfermedades foliares."
            )
        }
    }
    private fun cambiarEstado(titulo: String, descripcion: String) {
        binding.tvEstadoprocess.text = titulo
        binding.tvDescriprocess.text = descripcion
    }

    private fun observarEstado() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        AnalisisModeloUiState.Idle -> {
                            binding.lottieLoading.visibility = View.VISIBLE
                        }

                        is AnalisisModeloUiState.Procesando -> {
                            binding.lottieLoading.visibility = View.VISIBLE

                        }

                        is AnalisisModeloUiState.Exito -> {
                            procesoJob?.cancel()

                            binding.lottieLoading.visibility = View.GONE
                            irAResultado(state.label, state.confianza)
                        }

                        is AnalisisModeloUiState.DiagnosticoIncierto -> {
                            procesoJob?.cancel()

                            mostrarError(state.mensaje, esIncierto = true)
                        }

                        is AnalisisModeloUiState.ImagenNoTomate -> {
                            procesoJob?.cancel()
                            mostrarRechazo(state.mensaje)
                        }

                        is AnalisisModeloUiState.Error -> {
                            procesoJob?.cancel()

                            mostrarError(state.mensaje)
                        }
                    }
                }
            }
        }
    }

    /**
     * Muestra el rechazo directamente en ProcesamientoActivity.
     * No se genera diagnóstico, no se guarda en historial y no se cambia de pantalla
     * automáticamente. El usuario decide cuándo tomar otra fotografía.
     */
    private fun mostrarRechazo(mensaje: String) {
        binding.lottieLoading.visibility = View.GONE

        binding.tvTitulo.text = "Imagen no válida"
        binding.tvEstadoprocess.text = "No corresponde a una hoja enferma"
        binding.tvEstadoprocess.setTextColor(
            ContextCompat.getColor(this, R.color.warningOrange)
        )
        binding.tvDescriprocess.text =
            "La Inteligencia Artificial dice..."
        binding.tvConfianza.visibility = View.GONE

        binding.layoutRechazo.visibility = View.VISIBLE
        // Este llamado se hace desde ClasificarImagenUseCase
        binding.tvMensajeRechazo.text = mensaje
       // binding.tvDetalleRechazo.text = "Toma una nueva fotografía enfocando claramente una hoja de tomate."

        binding.btnNuevaFoto.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarError(mensaje: String, esIncierto:
    Boolean = false) {
        binding.lottieLoading.visibility = View.GONE
        binding.tvEstadoprocess.text = mensaje
        binding.tvDescriprocess.text = if (esIncierto) {
            "La Inteligencia Artificial dice..."
        } else {
            "Puedes regresar y seleccionar otra imagen."
        }
        binding.tvEstadoprocess.setTextColor(
            getColor(if (esIncierto) android.R.color.holo_purple
            else android.R.color.holo_red_dark)
        )
        // Mostrar el contenedor que incluye el botón
        binding.layoutRechazo.visibility = View.VISIBLE

        // Configurar el mensaje del contenedor cuando la hoja es enferma pero no esta bien enocada
        binding.tvMensajeRechazo.text = "Toma otra fotografía más cercana, enfocando claramente a la hoja de tomate."

       /* binding.tvDetalleRechazo.text = if (esIncierto) {
            "Toma otra fotografía más cercana, enfocando claramente una hoja de tomate."
        } else {
            "Puedes regresar a la cámara e intentar nuevamente."
        }*/

        // Asegurar que el botón esté visible y habilitado
        binding.btnNuevaFoto.visibility = View.VISIBLE
        binding.btnNuevaFoto.isEnabled = true

        // Regresar a la cámara
        binding.btnNuevaFoto.setOnClickListener {
            val intent = Intent(this@ProcesamientoActivity, CameraActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
            }

            startActivity(intent)
            finish()
        }
    }



    private fun irAResultado(resultadoModelo: String, confianza: Int) {
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val diagnosticoTraducido = traducir(resultadoModelo)

        viewModel.guardarEnHistorial(
            nombreModelo = resultadoModelo,
            confianza = confianza,
            imagenUri = imageUri
        )

        val intent = Intent(this, ResultadoAnalisisActivity::class.java).apply {
            putExtra(ResultadoAnalisisActivity.EXTRA_RESULTADO_MODELO, resultadoModelo)
            putExtra(ResultadoAnalisisActivity.EXTRA_RESULTADO_TRADUCIDO, diagnosticoTraducido)
            putExtra(ResultadoAnalisisActivity.EXTRA_CONFIANZA, confianza)
            putExtra(ResultadoAnalisisActivity.EXTRA_IMAGE_URI, imageUri)
        }

        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        procesoJob?.cancel()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "ProcesamientoIA"
        const val EXTRA_IMAGE_URI = "image_uri"
    }
}
