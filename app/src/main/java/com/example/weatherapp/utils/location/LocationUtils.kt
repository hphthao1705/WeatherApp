package com.example.weatherapp.utils.location

import android.location.Location
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

object LocationUtils {
    val STORAGE_REQUEST_CODE = 1000
    fun getCallBack(onSuccess: (location: Location?) -> Unit): LocationCallback {
        val mCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {

                onSuccess(p0.lastLocation)
            }

            override fun onLocationAvailability(p0: LocationAvailability) {


            }

        }
        return mCallback
    }

//    fun setMarkerLocation(map: GoogleMap, context: Context, latLng: LatLng) {
//        when {
//            ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED -> {
//                return
//            }
//            ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//                // user chose get location APPROXIMATE
//                map.isMyLocationEnabled = false
//                map.addCircle(
//                    CircleOptions().center(latLng)
//                        .radius(600.0)
//                        .strokeWidth(10F)
//                        .strokeColor(Color.TRANSPARENT)
//                        .fillColor(Color.argb(33, 0, 0, 255))
//                        .clickable(true)
//                )
//            }
//            else -> {
//                // user chose get location PRECISE
//                map.isMyLocationEnabled = true
//            }
//        }
//    }
}