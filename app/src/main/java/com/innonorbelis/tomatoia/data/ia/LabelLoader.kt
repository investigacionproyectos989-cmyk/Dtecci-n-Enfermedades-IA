package com.innonorbelis.tomatoia.data.ia

import android.content.Context

class LabelLoader(context: Context) {

    val labels: Map<Int, String>

    init {
        val temp = context.assets.open("labels.txt")
            .bufferedReader()
            .readLines()
            .mapIndexed { index, label -> index to label }
            .toMap()

        labels = temp
    }
}