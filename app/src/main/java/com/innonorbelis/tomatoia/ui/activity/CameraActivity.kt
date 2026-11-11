package com.innonorbelis.tomatoia.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.innonorbelis.tomatoia.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService

    private var camera: Camera? = null

    companion object {
        private const val TAG = "CameraActivity"
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Se necesita permiso de cámara.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    /*
     * Selecciona una imagen de la galería y conserva
     * el permiso de lectura cuando Android lo permite.
     */
    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->

            if (uri == null) {
                return@registerForActivityResult
            }

            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (error: SecurityException) {
                Log.w(
                    TAG,
                    "No se pudo conservar el permiso de lectura de la imagen.",
                    error
                )
            } catch (error: IllegalArgumentException) {
                Log.w(
                    TAG,
                    "El proveedor no permite conservar el permiso de la imagen.",
                    error
                )
            }

            abrirVistaPrevia(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        configurarEventos()
        verificarPermisoCamara()
    }

    private fun configurarEventos() {

        binding.ivclose.setOnClickListener {
            finish()
        }

        binding.ivregresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.ivCapture.setOnClickListener {
            tomarFoto()
        }

        binding.ivGallery.setOnClickListener {
            galleryLauncher.launch(arrayOf("image/*"))
        }

        binding.previewView.setOnTouchListener { view, event ->

            if (event.action == MotionEvent.ACTION_UP) {
                enfocarEn(event.x, event.y)
                view.performClick()
            }

            true
        }
    }

    private fun verificarPermisoCamara() {

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            binding.previewView.post {
                startCamera()
            }

        } else {

            cameraPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    private fun startCamera() {

        val future = ProcessCameraProvider.getInstance(this)

        future.addListener({

            try {

                val provider = future.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(
                            binding.previewView.surfaceProvider
                        )
                    }

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(
                        ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
                    )
                    .build()

                provider.unbindAll()

                camera = provider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )

            } catch (error: Exception) {

                Log.e(
                    TAG,
                    "No se pudo iniciar la cámara.",
                    error
                )

                Toast.makeText(
                    this,
                    "No se pudo iniciar la cámara.",
                    Toast.LENGTH_LONG
                ).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun enfocarEn(x: Float, y: Float) {

        val cam = camera ?: return

        val point = binding.previewView
            .meteringPointFactory
            .createPoint(x, y)

        val action = FocusMeteringAction.Builder(
            point,
            FocusMeteringAction.FLAG_AF
        )
            .setAutoCancelDuration(
                3,
                TimeUnit.SECONDS
            )
            .build()

        cam.cameraControl.startFocusAndMetering(action)
    }

    private fun tomarFoto() {

        if (!::imageCapture.isInitialized) {

            Toast.makeText(
                this,
                "La cámara todavía se está preparando.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val directorio = getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )

        if (directorio == null) {

            Toast.makeText(
                this,
                "No se pudo acceder al almacenamiento.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        val nombreArchivo = SimpleDateFormat(
            "yyyyMMdd_HHmmss_SSS",
            Locale.US
        ).format(System.currentTimeMillis()) + ".jpg"

        val photoFile = File(
            directorio,
            nombreArchivo
        )

        val output = ImageCapture.OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            output,
            ContextCompat.getMainExecutor(this),

            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(
                    output: ImageCapture.OutputFileResults
                ) {

                    abrirVistaPrevia(
                        Uri.fromFile(photoFile)
                    )
                }

                override fun onError(
                    exception: ImageCaptureException
                ) {

                    Log.e(
                        TAG,
                        "Error al guardar la fotografía.",
                        exception
                    )

                    Toast.makeText(
                        this@CameraActivity,
                        "No se pudo tomar la fotografía.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    private fun abrirVistaPrevia(uri: Uri) {

        val intent = Intent(
            this,
            VistaPreimageActivity::class.java
        ).apply {

            putExtra(
                "image_uri",
                uri.toString()
            )
        }

        startActivity(intent)
    }

    override fun onDestroy() {

        camera = null

        if (::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }

        super.onDestroy()
    }
}