package com.innonorbelis.tomatoia.core.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

fun View.aniBottom() {
    translationY = height.toFloat().coerceAtLeast(100f)

    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        translationY,
        0f
    ).apply {
        duration = 1000
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}

fun View.aniBottomtop() {
    translationY = height.toFloat().coerceAtLeast(100f)

    ObjectAnimator.ofFloat(
        this,
        View.TRANSLATION_Y,
        translationY,
        0f
    ).apply {
        duration = 1000
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}