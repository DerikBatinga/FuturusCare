package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

data class DetectedLocation(
    val address: String,
    val neighborhood: String,
    val city: String
)

object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    suspend fun detectCurrentLocation(context: Context): Result<DetectedLocation> {
        if (!hasLocationPermission(context)) {
            return Result.failure(SecurityException("Permissão de localização não concedida"))
        }

        val fusedClient = LocationServices.getFusedLocationProviderClient(context.applicationContext)

        val location: Location? = suspendCancellableCoroutine { cont ->
            val cts = CancellationTokenSource()
            cont.invokeOnCancellation { cts.cancel() }

            fusedClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
                .addOnSuccessListener { current ->
                    if (current != null) {
                        if (cont.isActive) cont.resume(current)
                    } else {
                        fusedClient.lastLocation
                            .addOnSuccessListener { last ->
                                if (cont.isActive) cont.resume(last)
                            }
                            .addOnFailureListener {
                                if (cont.isActive) cont.resume(null)
                            }
                    }
                }
                .addOnFailureListener {
                    fusedClient.lastLocation
                        .addOnSuccessListener { last ->
                            if (cont.isActive) cont.resume(last)
                        }
                        .addOnFailureListener {
                            if (cont.isActive) cont.resume(null)
                        }
                }
        }

        if (location == null) {
            return Result.failure(
                IllegalStateException("Não foi possível obter a localização do dispositivo agora.")
            )
        }

        return withContext(Dispatchers.IO) {
            val resolved = reverseGeocode(context, location.latitude, location.longitude)
            if (resolved != null) {
                Result.success(resolved)
            } else {
                Result.success(
                    DetectedLocation(
                        address = String.format(
                            Locale.getDefault(),
                            "Lat %.4f, Lon %.4f",
                            location.latitude,
                            location.longitude
                        ),
                        neighborhood = "Localização atual",
                        city = "Brasil"
                    )
                )
            }
        }
    }

    private suspend fun reverseGeocode(
        context: Context,
        latitude: Double,
        longitude: Double
    ): DetectedLocation? {
        if (!Geocoder.isPresent()) return null
        val geocoder = Geocoder(context, Locale("pt", "BR"))

        return try {
            val address: Address? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { cont ->
                    geocoder.getFromLocation(latitude, longitude, 1) { list ->
                        if (cont.isActive) cont.resume(list.firstOrNull())
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
            }

            address?.let { addr ->
                val street = listOfNotNull(
                    addr.thoroughfare?.takeIf { it.isNotBlank() },
                    addr.subThoroughfare?.takeIf { it.isNotBlank() }
                ).joinToString(", ")

                val neighborhood = addr.subLocality
                    ?.takeIf { it.isNotBlank() }
                    ?: addr.subAdminArea?.takeIf { it.isNotBlank() }
                    ?: "Centro"

                val city = addr.locality
                    ?.takeIf { it.isNotBlank() }
                    ?: addr.subAdminArea?.takeIf { it.isNotBlank() }
                    ?: addr.adminArea?.takeIf { it.isNotBlank() }
                    ?: "São Paulo"

                val finalAddress = street.ifBlank {
                    addr.getAddressLine(0)?.substringBefore(",")?.trim().orEmpty()
                }.ifBlank { "Próximo a $neighborhood" }

                DetectedLocation(
                    address = finalAddress,
                    neighborhood = neighborhood,
                    city = city
                )
            }
        } catch (_: Exception) {
            null
        }
    }
}
