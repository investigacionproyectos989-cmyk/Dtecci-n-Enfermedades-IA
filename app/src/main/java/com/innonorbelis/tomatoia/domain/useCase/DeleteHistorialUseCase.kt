package com.innonorbelis.tomatoia.domain.useCase

import com.innonorbelis.tomatoia.domain.repository.HistorialRepository
import javax.inject.Inject

class DeleteHistorialUseCase @Inject constructor(
    private val repository: HistorialRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.eliminarPorId(id)
    }
}
