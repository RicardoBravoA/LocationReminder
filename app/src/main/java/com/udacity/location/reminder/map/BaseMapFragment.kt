package com.udacity.location.reminder.map

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.udacity.location.reminder.R
import com.udacity.location.reminder.base.BaseFragment
import com.udacity.location.reminder.util.Constant
import com.udacity.location.reminder.util.GpsUtil
import kotlin.random.Random

abstract class BaseMapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    abstract fun locationCallback(): LocationCallback
    var location: Location? = null
    var map: GoogleMap? = null
    internal var markerList: MutableList<Marker> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            requestPermission()
        } else {
            buildLocationRequest()
            locationCallback()
            validatePermission()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        buildLocationRequest()
        locationCallback()
    }

    private fun activateGPS() {
        GpsUtil(requireContext()).turnOnGPS(object : GpsUtil.GpsListener {
            override fun onGpsStatus(isGPSEnable: Boolean) {
                validateLocation()
            }
        })
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latitude = 37.422160
        val longitude = -122.084270

        val defaultLocation = LatLng(latitude, longitude)

        val poi = PointOfInterest(
            defaultLocation,
            getString(R.string.googleplex),
            getString(R.string.googleplex)
        )

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, Constant.MAP_ZOOM))
        addMarker(
            poi,
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        )

        map?.uiSettings?.isZoomControlsEnabled = true

        setMapStyle()
        enableMyLocation()
    }

    fun addMarker(
        poi: PointOfInterest,
        icon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker(
            Random.nextInt(360).toFloat()
        )
    ): Marker? {
        val marker = map?.addMarker(
            MarkerOptions()
                .position(poi.latLng)
                .title(poi.name)
                .icon(icon)
        )
        marker?.apply {
            showInfoWindow()
            markerList.add(this)
            tag = markerList.size - 1
        }
        return marker
    }

    private fun setMapStyle() {
        try {
            map?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    private fun validateLocation() {
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            map?.isMyLocationEnabled = true
        }
        location?.let {
            moveCamera(LatLng(it.latitude, it.longitude))
        }
    }

    fun moveCamera(latLng: LatLng) {
        map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map?.animateCamera(CameraUpdateFactory.zoomTo(Constant.MAP_ZOOM))
    }

    private fun validatePermission() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission
                    .ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
        } else {
            activateGPS()
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback(),
            Looper.myLooper()
        )
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), Constant.REQUEST_CODE
        )
    }

    private fun enableMyLocation() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                activateGPS()
                map?.isMyLocationEnabled = true
            }
            else -> {
                requestPermission()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constant.REQUEST_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                activateGPS()
            }
        }
    }

}