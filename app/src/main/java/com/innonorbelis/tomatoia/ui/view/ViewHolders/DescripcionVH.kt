package com.innonorbelis.tomatoia.ui.view.ViewHolders

import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.databinding.ItemDescripcionBinding

class DescripcionVH(
    private val binding: ItemDescripcionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(descripcion: String) {

        binding.tvDescripcion.text = descripcion
    }
}