package com.innonorbelis.tomatoia.core.util

import com.innonorbelis.tomatoia.R

fun getDescription(code: Int?): String = when (code) {

    0 -> "Despejado"

    1 -> "Mayormente despejado"
    2 -> "Parcialmente nublado"
    3 -> "Nublado"

    45, 48 -> "Niebla"

    51, 53, 55 -> "Llovizna"
    56, 57 -> "Llovizna helada"

    61, 63, 65 -> "Lluvia"
    66, 67 -> "Lluvia helada"

    71, 73, 75 -> "Nevada"
    77 -> "Granizo"

    80, 81, 82 -> "Chubascos"

    85, 86 -> "Chubascos de nieve"

    95 -> "Tormenta"
    96, 99 -> "Tormenta con granizo"

    else -> "Clima desconocido"
}

fun getWeatherIcon(code: Int): Int {

    return when (code) {

        0 -> R.drawable.ic_sun

        1, 2 -> R.drawable.ic_partlycloudy
        3 -> R.drawable.ic_cloudyweather

        45, 48 -> R.drawable.ic_fog

        51, 53, 55, 56, 57 -> R.drawable.ic_drizzle

        61, 63, 65, 66, 67 -> R.drawable.ic_rain

        71, 73, 75, 77 -> R.drawable.ic_snow

        80, 81, 82 -> R.drawable.ic_showers

        85, 86 -> R.drawable.ic_snow

        95, 96, 99 -> R.drawable.ic_storm

        else -> R.drawable.ic_cloudyweather
    }
}