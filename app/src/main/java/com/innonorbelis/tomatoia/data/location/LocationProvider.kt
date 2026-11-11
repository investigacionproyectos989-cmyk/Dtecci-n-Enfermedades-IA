package com.innonorbelis.tomatoia.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationProvider(context: Context) {

    private val fused = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Pair<Double, Double>? =
        suspendCancellableCoroutine { cont ->
            fused.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) cont.resume(loc.latitude to loc.longitude)
                else cont.resume(null)
            }.addOnFailureListener {
                cont.resume(null)
            }
        }
}