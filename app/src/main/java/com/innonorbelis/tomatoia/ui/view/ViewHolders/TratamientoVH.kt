package com.innonorbelis.tomatoia.ui.view.ViewHolders

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.innonorbelis.tomatoia.databinding.ItemTratamientoBinding
import com.innonorbelis.tomatoia.domain.model.Tratamiento
import com.innonorbelis.tomatoia.ui.adapter.TratamientoAdapter

class TratamientoVH(
    private val binding: ItemTratamientoBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = TratamientoAdapter()

    init {

        binding.rvTratamientos.apply {

            layoutManager =
                LinearLayoutManager(binding.root.context)

            adapter = this@TratamientoVH.adapter

            setHasFixedSize(true)

            isNestedScrollingEnabled = false
        }
    }

    fun bind(
        lista: List<Tratamiento>,
        expanded: Boolean,
        onToggle: () -> Unit
    ) {

        Log.d(
            "TRATAMIENTO_DEBUG",
            "Cantidad tratamientos: ${lista.size}"
        )

        Log.d(
            "TRATAMIENTO_DEBUG",
            lista.toString()
        )

        adapter.submitList(lista)

        binding.expandableTratamiento.visibility =
            if (expanded)
                View.VISIBLE
            else
                View.GONE

        binding.tvToggleLabel.text =
            if (expanded)
                "Ver menos"
            else
                "Ver más"

        binding.imgChevron.rotation =
            if (expanded)
                180f
            else
                0f

        binding.headerTratamiento.setOnClickListener {

            TransitionManager.beginDelayedTransition(
                binding.root,
                AutoTransition()
            )

            onToggle()
        }
    }
}