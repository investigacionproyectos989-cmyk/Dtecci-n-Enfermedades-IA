package com.innonorbelis.tomatoia.domain.useCase

import com.innonorbelis.tomatoia.domain.model.DiseaseRisk
import com.innonorbelis.tomatoia.domain.model.Weather
import javax.inject.Inject

class CalculateDiseaseRiskUseCase @Inject constructor(){
    operator fun invoke (weather: Weather?): DiseaseRisk{
        return  when{
            weather?.humidity!! >=85 &&
                    weather.rainProb >= 50-> DiseaseRisk.MODERATE
            else-> DiseaseRisk.LOW
        }
    }
}