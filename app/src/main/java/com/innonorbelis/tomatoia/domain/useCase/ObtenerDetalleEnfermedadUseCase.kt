package com.innonorbelis.tomatoia.domain.useCase

import com.innonorbelis.tomatoia.domain.repository.EnfermedadRepository
import javax.inject.Inject


class ObtenerDetalleEnfermedadUseCase @Inject constructor(
    private val repository: EnfermedadRepository
) {

    suspend operator fun invoke(
        nombreModelo: String
    ) = repository.obtenerDetalleEnfermedad(nombreModelo)
}

