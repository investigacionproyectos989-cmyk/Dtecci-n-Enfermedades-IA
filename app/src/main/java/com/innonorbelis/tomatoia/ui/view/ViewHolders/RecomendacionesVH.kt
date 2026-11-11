package com.innonorbelis.tomatoia.ui.view.ViewHolders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.databinding.ItemRecomendacionesBinding
import com.innonorbelis.tomatoia.ui.adapter.RecomendacionAdapter

class RecomendacionesVH(
    private val binding: ItemRecomendacionesBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter =
        RecomendacionAdapter()

    init {

        binding.rvRecomendaciones.layoutManager =
            LinearLayoutManager(binding.root.context)

        binding.rvRecomendaciones.adapter =
            adapter
    }

    fun bind(lista: List<String>) {

        adapter.submitList(lista)
    }
}