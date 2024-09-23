package com.example.weatherapp.utils.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherapp.utils.LOCATION_PERMISSION
import com.example.weatherapp.utils.Prefs
import com.example.weatherapp.view.activity.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationProvider(
    var activity: MainActivity,
    var fragment: Fragment,
    var listener: LocationCallback
) {
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    val TAG: String = "Location"
    fun onLocation() {
        if (!checkGPSEnabled()) {
            return
        }
        //

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                getLocation()
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                getLocation()
            } else {
                //Request Location Permission
                checkLocationPermissionAPI30()
            }
        } else {
            getLocation()
        }
    }

    fun isHadPermission(): LOCATION_PERMISSION {
        if (!checkGPSEnabled()) {
            return LOCATION_PERMISSION.NO_LOCATION_SERVICE
        } else {
            if (checkAppPermission()) {
                return LOCATION_PERMISSION.OK
            } else {
                return LOCATION_PERMISSION.NO_PERMISSION
            }
        }
    }

    fun checkAppPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            return true
        }
    }

    fun checkPermissionType(): String {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return Manifest.permission.ACCESS_FINE_LOCATION
        } else {
            return Manifest.permission.ACCESS_COARSE_LOCATION
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Prefs.permissionType = Manifest.permission.ACCESS_FINE_LOCATION
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it == null) {
                    startLocationUpdates()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //user chose get location APPROXIMATE
                        onLocation()
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        startLocationUpdates()
                    } else {
                        listener.onLocationResult(LocationResult.create(arrayListOf(it)))
                    }
                }
            }.addOnFailureListener {
                startLocationUpdates()
            }
        }
    }

    fun startLocationUpdates() {
        // Create the location request
        val mLocationRequest: LocationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .build()
        // Request location updates
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mLocationRequest.let {
            fusedLocationClient.requestLocationUpdates(it, listener, Looper.getMainLooper())
        }
    }


    private fun checkGPSEnabled(): Boolean {
        return if (!isLocationEnabled()) {
            activity.apply {
//                showCustomAlert(
//                    locationDisableText,
//                    settingText
//                ) {
//                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                    this.startActivity(myIntent)
//                }
            }
            false
        } else {
            true
        }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    var permissionLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                onLocation()
                Prefs.permissionType = Manifest.permission.ACCESS_FINE_LOCATION
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                onLocation()
                Prefs.permissionType = Manifest.permission.ACCESS_COARSE_LOCATION
            }

            else -> {
                // No location access granted.
            }
        }
        activity.hideAlert()
    }


    fun checkLocationPermission() {

        val grantCode =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (grantCode
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

            } else {

                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkLocationPermissionAPI30() {
        val grantCode =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
//        val grantCode2 = ContextCompat.checkSelfPermission(
//            activity,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION
//        )
        val grantCode3 =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (grantCode != PackageManager.PERMISSION_GRANTED && grantCode3 != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
//                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            }
        }
    }

    fun removeGetLocation() {
        fusedLocationClient.removeLocationUpdates(listener)
    }
}