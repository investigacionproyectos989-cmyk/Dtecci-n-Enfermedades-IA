package com.innonorbelis.tomatoia.ui.view.ViewHolders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.databinding.ItemSintomasBinding
import com.innonorbelis.tomatoia.ui.adapter.SintomasAdapter

class SintomasVH(
    private val binding: ItemSintomasBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = SintomasAdapter()

    init {

        binding.rvSintomas.layoutManager =
            LinearLayoutManager(binding.root.context)

        binding.rvSintomas.adapter =
            adapter
    }

    fun bind(lista: List<String>) {

        adapter.submitList(lista)
    }
}