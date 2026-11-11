package com.innonorbelis.tomatoia.domain.useCase

import com.innonorbelis.tomatoia.domain.model.Historial
import com.innonorbelis.tomatoia.domain.repository.HistorialRepository
import javax.inject.Inject

class SaveHistorialUseCase @Inject constructor(
    private val repository: HistorialRepository
) {

    suspend operator fun invoke(
        historial: Historial
    ) {
        repository.guardarHistorial(historial)
    }
}