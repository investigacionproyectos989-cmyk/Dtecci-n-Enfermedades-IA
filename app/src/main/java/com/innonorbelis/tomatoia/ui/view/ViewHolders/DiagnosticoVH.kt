package com.innonorbelis.tomatoia.ui.view.ViewHolders

import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.innonorbelis.tomatoia.databinding.ItemDiagnosticoBinding
import com.innonorbelis.tomatoia.domain.model.Diagnostico

class DiagnosticoVH(
    private val binding: ItemDiagnosticoBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var progressAnimator: ObjectAnimator? = null

    fun bind(d: Diagnostico) {
        val confianza = d.confianza.coerceIn(0, 100)

        binding.tvNombreEnfermedad.text = d.nombre

        val cientifico = d.nombreCientifico?.trim().orEmpty()
        if (cientifico.isBlank()) {
            binding.tvNombreCientifico.visibility = android.view.View.GONE
        } else {
            binding.tvNombreCientifico.visibility = android.view.View.VISIBLE
            binding.tvNombreCientifico.text = cientifico
        }

        binding.tvConfianza.text = "$confianza%"
        binding.progressConfianza.max = 100

        // La barra representa exactamente el porcentaje recibido por el modelo.
        // Se anima desde el valor anterior hasta el nuevo para que el cambio sea visible.
        progressAnimator?.cancel()
        val anterior = binding.progressConfianza.progress
        progressAnimator = ObjectAnimator.ofInt(
            binding.progressConfianza,
            "progress",
            anterior,
            confianza
        ).apply {
            duration = 500L
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    fun clearAnimation() {
        progressAnimator?.cancel()
        progressAnimator = null
    }
}
