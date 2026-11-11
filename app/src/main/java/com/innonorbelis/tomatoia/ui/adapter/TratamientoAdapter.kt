package com.innonorbelis.tomatoia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.databinding.ItemTratamientoRowBinding
import com.innonorbelis.tomatoia.domain.model.Tratamiento

class TratamientoAdapter :
    ListAdapter<Tratamiento, TratamientoAdapter.VH>(
        DiffCallback()
    ) {

    class VH(
        val binding: ItemTratamientoRowBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {

        val binding =
            ItemTratamientoRowBinding.inflate(
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

        val item = getItem(position)

        holder.binding.tvIngrediente.text =
            item.ingredienteActivo

        holder.binding.tvProducto.text =
            item.producto

        holder.binding.tvDosis.text =
            item.dosis

        holder.binding.tvPC.text =
            item.pc.toString()

        holder.binding.tvPR.text =
            item.pr.toString()
    }

    class DiffCallback :
        DiffUtil.ItemCallback<Tratamiento>() {

        override fun areItemsTheSame(
            oldItem: Tratamiento,
            newItem: Tratamiento
        ): Boolean {

            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Tratamiento,
            newItem: Tratamiento
        ): Boolean {

            return oldItem == newItem
        }
    }
}