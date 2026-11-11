package com.innonorbelis.tomatoia.ui.view

import com.innonorbelis.tomatoia.R
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.innonorbelis.tomatoia.databinding.FragmentInicioBinding
import com.innonorbelis.tomatoia.data.location.LocationProvider
import com.innonorbelis.tomatoia.ui.viewmodel.WeatherUiState
import com.innonorbelis.tomatoia.ui.viewmodel.WeatherViewModel
import com.innonorbelis.tomatoia.core.util.getDescription
import com.innonorbelis.tomatoia.core.util.getWeatherIcon
import com.innonorbelis.tomatoia.domain.model.DiseaseRisk
import com.innonorbelis.tomatoia.ui.activity.CameraActivity
import com.innonorbelis.tomatoia.ui.activity.DialogOpcionSiembraActivity
import com.innonorbelis.tomatoia.ui.activity.RecordatorioActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

import java.text.SimpleDateFormat
import java.util.Date



@AndroidEntryPoint
class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var locationProvider: LocationProvider

    /* Función View*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    //@SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationProvider = LocationProvider(requireContext())

        observeWeather()

        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        // boton para boton de camara
        binding.btnCamera.setOnClickListener {activityCamara()}
        // boton de recordatorio
        //binding.ivTomaterecordatorio.setOnClickListener {recordatorioControl()}
        // Boton registrar simbra
        //binding.btnSiembra.setOnClickListener { activityCreaSiembra()}

        //  DATE
        val fechaActual = SimpleDateFormat(
            "EEEE, d 'de' MMMM 'de' yyyy",
            Locale("es", "ES")
        ).format(Date())

        binding.tvGreetingDate.text =
            fechaActual.replaceFirstChar { it.uppercase(Locale("es", "ES")) }

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeWeather() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.uiState.collect { state ->

                when (state) {

                    is WeatherUiState.Loading -> {

                        binding.loadingWeather.visibility = View.VISIBLE
                    }

                    is WeatherUiState.Success -> {

                        binding.apply {

                            loadingWeather.visibility = View.GONE

                            val weather = state.weather

                            if (weather != null) {

                                tvCurrentTemp.text =
                                    "${weather.temperature?.roundToInt() ?: "--"}°C"

                                tvWeatherTemp.text =
                                    getDescription(weather.weathercode)

                                ivWeatherIcon.setImageResource(
                                    getWeatherIcon(weather.weathercode ?: 0)
                                )

                                // Actualizar el riesgo de enfermedades foliares
                                when (viewModel.getRisk(weather)) {

                                    DiseaseRisk.HIGH -> {

                                        tvRisk.text =
                                            "Alto riesgo enfermedad foliar"

                                        tvRisk.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.risk_high
                                            )
                                        )
                                    }

                                    DiseaseRisk.MODERATE -> {

                                        tvRisk.text =
                                            "Riesgo moderado enfermedad foliar"

                                        tvRisk.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.risk_moderate
                                            )
                                        )
                                    }

                                    DiseaseRisk.LOW -> {

                                        tvRisk.text =
                                            "Bajo riesgo enfermedad foliar"

                                        tvRisk.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.risk_low
                                            )
                                        )
                                    }
                                }
                            } else {

                                mostrarUbicacionNoDisponible()
                            }
                        }
                    }

                    is WeatherUiState.Error -> {

                        // Ocultar el indicador de carga
                        binding.loadingWeather.visibility = View.GONE

                        // Mostrar el mensaje dentro de la tarjeta
                        mostrarUbicacionNoDisponible()
                    }
                }
            }
        }
    }

    private fun mostrarUbicacionNoDisponible() {

        if (_binding == null) return

        binding.apply {

            // Detener el indicador de carga
            loadingWeather.visibility = View.GONE

            // Mostrar la información de ubicación no disponible
            tvLocation.text = "Ubicación no disponible"

            // Mostrar un texto neutral para la temperatura
            tvCurrentTemp.text = "--°C"

            // Mensaje dentro de la tarjeta
            tvWeatherTemp.text = "Sin conexión a Internet"

            // Evitar mostrar información meteorológica desactualizada
            tvRisk.text = "Información meteorológica no disponible"
        }
    }

   /* Localización ---------------- */

    private val requestPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                loadWeatherWithGPS()
            } else {
                mostrarUbicacionNoDisponible()
            }
        }

    private fun loadWeatherWithGPS() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {

                val loc = locationProvider.getCurrentLocation()

                if (loc != null) {

                    val (lat, lon) = loc

                    // Consultar el clima de la ubicación actual
                    viewModel.load(lat, lon)

                    // Obtener el municipio
                    val municipio = getMunicipality(lat, lon)

                    if (_binding != null) {

                        binding.tvLocation.text =
                            municipio.ifBlank {
                                "Ubicación no disponible"
                            }
                    }

                } else {

                    mostrarUbicacionNoDisponible()
                }

            } catch (e: Exception) {

                mostrarUbicacionNoDisponible()
            }
        }
    }
    private suspend fun getMunicipality(lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(requireContext(), Locale("es"))
            val list = geocoder.getFromLocation(lat, lon, 1)

            if (!list.isNullOrEmpty()) {
                val address = list[0]

                val municipio =
                    address.locality ?: address.subAdminArea ?: ""

                val departamento =
                    address.adminArea ?: ""

                buildString {
                    if (municipio.isNotEmpty()) append("$municipio, ")
                    if (departamento.isNotEmpty()) append(departamento)
                }.trim().trim(',')
            } else {
                "Ubicación no disponible"
            }

        } catch (e: Exception) {
            "Ubicación no disponible"
        }
    }

    //------INTENT ACTIVIDAD DE OPCIONES SIEMBRA

    private fun activityCreaSiembra() {
        //findNavController().navigate(R.id.action_inicio_to_camera)
        val intent = Intent(requireContext(), DialogOpcionSiembraActivity::class.java)
        startActivity(intent)
    }
//--- funcion camara--------------------------
    private fun activityCamara() {
       //findNavController().navigate(R.id.action_inicio_to_camera)
    val intent = Intent(requireContext(), CameraActivity::class.java)
    startActivity(intent)
    }

    private fun recordatorioControl() {
        val intent = Intent(requireContext(), RecordatorioActivity::class.java)
        startActivity(intent)
    }

     override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
  }




