package com.innonorbelis.tomatoia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.databinding.ItemRecomendacionBinding

class RecomendacionAdapter :
    ListAdapter<String, RecomendacionAdapter.VH>(
        DiffCallback()
    ) {

    class VH(
        val binding: ItemRecomendacionBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {

        val binding = ItemRecomendacionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return VH(binding)
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int
    ) {

        holder.binding.tvRecomendacion.text =
            getItem(position)
    }

    class DiffCallback :
        DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {

            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {

            return oldItem == newItem
        }
    }
}