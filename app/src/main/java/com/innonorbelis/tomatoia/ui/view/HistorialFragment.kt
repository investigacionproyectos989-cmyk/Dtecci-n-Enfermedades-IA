package com.innonorbelis.tomatoia.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.innonorbelis.tomatoia.databinding.FragmentHistorialBinding
import com.innonorbelis.tomatoia.domain.model.Historial
import com.innonorbelis.tomatoia.ui.activity.ResultadoAnalisisActivity
import com.innonorbelis.tomatoia.ui.adapter.HistorialAdapter
import com.innonorbelis.tomatoia.ui.viewmodel.HistorialViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistorialFragment : Fragment() {

    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!

    private val viewModel:
            HistorialViewModel by viewModels()

    private lateinit var historialAdapter:
            HistorialAdapter

    private val filtros = listOf(

        "Todas",

        "Mancha bacteriana",

        "Tizón temprano",

        "Tizón tardío (Gota)",

        "Moho de la hoja",

        "Septoria",

        "Mancha de hierro",

        "Mildéu polvoroso",

    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentHistorialBinding.inflate(
                inflater,
                container,
                false
            )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        configurarRecyclerView()

        configurarFiltro()

        observarHistorial()
    }

    private fun configurarRecyclerView() {

        historialAdapter = HistorialAdapter(
            onItemClick = { historial ->
                abrirDetalle(historial)
            },
            onDeleteClick = { historial ->
                confirmarEliminacion(historial)
            }
        )

        binding.rvHistorial.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = historialAdapter
        }
    }

    private fun configurarFiltro() {

        val adapterSpinner =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                filtros
            )

        adapterSpinner.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerFiltro.adapter =
            adapterSpinner

        binding.spinnerFiltro.onItemSelectedListener =
            object :
                android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent:
                    android.widget.AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position in filtros.indices) {
                        viewModel.seleccionarFiltro(filtros[position])
                    }
                }

                override fun onNothingSelected(
                    parent:
                    android.widget.AdapterView<*>?
                ) {
                    // No se requiere acción
                }
            }
    }

    private fun observarHistorial() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.historial.collect { lista ->

                    mostrarHistorial(lista)
                }
            }
        }
    }

    private fun mostrarHistorial(
        lista: List<Historial>
    ) {

        historialAdapter.submitList(lista)

        binding.tvCantidad.text =
            if (lista.size == 1) {
                "1 análisis encontrado"
            } else {
                "${lista.size} análisis encontrados"
            }

        binding.tvSinHistorial.visibility =
            if (lista.isEmpty())
                View.VISIBLE
            else
                View.GONE

        binding.rvHistorial.visibility =
            if (lista.isEmpty())
                View.GONE
            else
                View.VISIBLE
    }

    private fun confirmarEliminacion(historial: Historial) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar diagnóstico")
            .setMessage("¿Deseas eliminar este diagnóstico del historial? Esta acción no se puede deshacer.")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarHistorial(historial.id)
            }
            .show()
    }

    private fun abrirDetalle(
        historial: Historial
    ) {

        val intent =
            Intent(
                requireContext(),
                ResultadoAnalisisActivity::class.java
            ).apply {

                putExtra(
                    "resultado_modelo",
                    historial.nombreModelo
                )

                putExtra(
                    "resultado_traducido",
                    historial.nombreMostrar
                )

                putExtra(
                    "confianza",
                    historial.confianza
                )

                putExtra(
                    "image_uri",
                    historial.imagenUri
                )

                putExtra(
                    "modo_historial",
                    true
                )
            }

        startActivity(intent)
    }

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}