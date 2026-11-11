package com.innonorbelis.tomatoia.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.databinding.ItemDescripcionBinding
import com.innonorbelis.tomatoia.databinding.ItemDiagnosticoBinding
import com.innonorbelis.tomatoia.databinding.ItemRecomendacionesBinding
import com.innonorbelis.tomatoia.databinding.ItemSintomasBinding
import com.innonorbelis.tomatoia.databinding.ItemTratamientoBinding
import com.innonorbelis.tomatoia.domain.model.DashboardItem
import com.innonorbelis.tomatoia.ui.view.ViewHolders.DescripcionVH
import com.innonorbelis.tomatoia.ui.view.ViewHolders.DiagnosticoVH
import com.innonorbelis.tomatoia.ui.view.ViewHolders.RecomendacionesVH
import com.innonorbelis.tomatoia.ui.view.ViewHolders.SintomasVH
import com.innonorbelis.tomatoia.ui.view.ViewHolders.TratamientoVH

class DashboardAdapter :
    ListAdapter<DashboardItem, RecyclerView.ViewHolder>(
        DashboardDiffCallback()
    ) {

    companion object {

        private const val DIAGNOSTICO = 0
        private const val DESCRIPCION = 1
        private const val SINTOMAS = 2
        private const val RECOMENDACIONES = 3
        private const val TRATAMIENTO = 4
    }

    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)) {

            is DashboardItem.DiagnosticoItem -> DIAGNOSTICO

            is DashboardItem.DescripcionItem -> DESCRIPCION

            is DashboardItem.SintomasItem -> SINTOMAS

            is DashboardItem.RecomendacionesItem -> RECOMENDACIONES

            is DashboardItem.TratamientoItem -> TRATAMIENTO
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val inflater =
            LayoutInflater.from(parent.context)

        return when (viewType) {

            DIAGNOSTICO -> {

                val binding =
                    ItemDiagnosticoBinding.inflate(
                        inflater,
                        parent,
                        false
                    )

                DiagnosticoVH(binding)
            }

            DESCRIPCION -> {

                val binding =
                    ItemDescripcionBinding.inflate(
                        inflater,
                        parent,
                        false
                    )

                DescripcionVH(binding)
            }

            SINTOMAS -> {

                val binding =
                    ItemSintomasBinding.inflate(
                        inflater,
                        parent,
                        false
                    )

                SintomasVH(binding)
            }

            RECOMENDACIONES -> {

                val binding =
                    ItemRecomendacionesBinding.inflate(
                        inflater,
                        parent,
                        false
                    )

                RecomendacionesVH(binding)
            }

            TRATAMIENTO -> {

                val binding =
                    ItemTratamientoBinding.inflate(
                        inflater,
                        parent,
                        false
                    )

                TratamientoVH(binding)
            }

            else -> {
                throw IllegalArgumentException("Tipo desconocido")
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        when (val item = getItem(position)) {

            is DashboardItem.DiagnosticoItem -> {

                (holder as DiagnosticoVH)
                    .bind(item.data)
            }

            is DashboardItem.DescripcionItem -> {

                (holder as DescripcionVH)
                    .bind(item.descripcion)
            }

            is DashboardItem.SintomasItem -> {

                (holder as SintomasVH)
                    .bind(item.lista)
            }

            is DashboardItem.RecomendacionesItem -> {

                (holder as RecomendacionesVH)
                    .bind(item.lista)
            }

            is DashboardItem.TratamientoItem -> {

                (holder as TratamientoVH)
                    .bind(
                        lista = item.tratamientos,
                        expanded = item.expanded,
                        onToggle = {

                            val nuevaLista =
                                currentList.toMutableList()

                            val itemActual =
                                item.copy(
                                    expanded = !item.expanded
                                )

                            nuevaLista[position] =
                                itemActual

                            submitList(nuevaLista)
                        }
                    )
            }
        }
    }
}