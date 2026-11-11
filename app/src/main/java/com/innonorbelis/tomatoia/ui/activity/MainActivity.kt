package com.innonorbelis.tomatoia.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.innonorbelis.tomatoia.R
import com.innonorbelis.tomatoia.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment

    private val cameraLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // Cuando la cámara se cierre, volver siempre a Inicio.
            if (isFinishing || isDestroyed) return@registerForActivityResult

            val navController = navHostFragment.navController
            if (navController.currentDestination?.id != R.id.page_inicio) {
                val returnedToHome = navController.popBackStack(R.id.page_inicio, false)
                if (!returnedToHome) {
                    Log.w(TAG, "No se pudo volver a Inicio después de cerrar la cámara")
                }
            }

            binding.bottomNavigationView.selectedItemId = R.id.page_inicio
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val bottomNavigationView = binding.bottomNavigationView

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_consultaIA -> {
                    cameraLauncher.launch(
                        Intent(this, CameraActivity::class.java)
                    )
                    true
                }

                else -> {
                    try {
                        NavigationUI.onNavDestinationSelected(item, navController)
                    } catch (error: IllegalArgumentException) {
                        Log.e(TAG, "No se pudo navegar al destino ${item.itemId}", error)
                        false
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
