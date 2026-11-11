package com.innonorbelis.tomatoia.domain.useCase

import com.innonorbelis.tomatoia.domain.model.Historial
import com.innonorbelis.tomatoia.domain.repository.HistorialRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistorialUseCase @Inject constructor(
    private val repository: HistorialRepository
) {

    operator fun invoke(): Flow<List<Historial>> {

        return repository.obtenerHistorial()
    }
}