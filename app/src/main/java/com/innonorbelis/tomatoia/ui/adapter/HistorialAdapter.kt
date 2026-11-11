package com.innonorbelis.tomatoia.ui.adapter

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.R
import com.innonorbelis.tomatoia.databinding.ItemHistorialBinding
import com.innonorbelis.tomatoia.domain.model.Historial
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistorialAdapter(
    private val onItemClick: (Historial) -> Unit,
    private val onDeleteClick: (Historial) -> Unit
) : RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder>() {

    private var lista = emptyList<Historial>()

    companion object {
        private const val TAG = "HistorialAdapter"
    }

    fun submitList(nuevaLista: List<Historial>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistorialViewHolder {

        val binding = ItemHistorialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HistorialViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HistorialViewHolder,
        position: Int
    ) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    inner class HistorialViewHolder(
        private val binding: ItemHistorialBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(historial: Historial) {

            binding.tvNombreEnfermedad.text =
                historial.nombreMostrar

            binding.tvConfianza.text =
                "Confianza: ${historial.confianza}%"

            binding.tvFecha.text =
                SimpleDateFormat(
                    "dd/MM/yyyy · hh:mm a",
                    Locale("es", "CO")
                ).format(
                    Date(historial.fecha)
                )

            cargarImagen(historial.imagenUri)

            binding.root.setOnClickListener {
                onItemClick(historial)
            }

            binding.btnVerDetalle.setOnClickListener {
                onItemClick(historial)
            }

            binding.btnEliminar.setOnClickListener {
                onDeleteClick(historial)
            }
        }

        private fun cargarImagen(uriTexto: String?) {

            // Mostrar una imagen predeterminada mientras se carga.
            binding.imgHoja.setImageResource(
                R.drawable.ic_hojatoma
            )

            if (uriTexto.isNullOrBlank()) {

                Log.w(
                    TAG,
                    "El registro del historial no contiene una URI de imagen."
                )

                return
            }

            try {

                val uri = Uri.parse(uriTexto)

                val bitmap = binding.root.context
                    .contentResolver
                    .openInputStream(uri)
                    ?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }

                if (bitmap != null) {

                    binding.imgHoja.setImageBitmap(bitmap)

                } else {

                    Log.e(
                        TAG,
                        "No se pudo decodificar la imagen: $uriTexto"
                    )

                    binding.imgHoja.setImageResource(
                        R.drawable.ic_hojatoma
                    )
                }

            } catch (error: Exception) {

                Log.e(
                    TAG,
                    "Error al cargar la imagen del historial: $uriTexto",
                    error
                )

                binding.imgHoja.setImageResource(
                    R.drawable.ic_hojatoma
                )
            }
        }
    }
}