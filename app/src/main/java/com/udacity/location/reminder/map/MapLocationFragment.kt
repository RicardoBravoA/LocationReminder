package com.udacity.location.reminder.map

import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.location.reminder.R
import com.udacity.location.reminder.databinding.FragmentMapLocationBinding
import com.udacity.location.reminder.save.SaveReminderViewModel
import com.udacity.location.reminder.util.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

class MapLocationFragment : BaseMapFragment(), GoogleMap.OnPoiClickListener {

    //Use Koin to get the view model of the SaveReminder
    override val viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentMapLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMapLocationBinding.inflate(inflater)

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


//        TODO: zoom to the user location after taking his permission
//        TODO: put a marker to location that the user selected


//        TODO: call this function after the user confirms on the selected location
        onLocationSelected()

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)

        map?.setOnPoiClickListener(this)
    }

    private fun onLocationSelected() {
        //        TODO: When the user confirms on the selected location,
        //         send back the selected location details to the view model
        //         and navigate back to the previous fragment to save the reminder and add the geofence
    }

    override fun locationCallback(): LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            location = locationResult!!.locations[locationResult.locations.size - 1]
            getData(location?.latitude.toString(), location?.longitude.toString())
        }
    }

    private fun getData(latitude: String? = null, longitude: String? = null) {
        if (location != null && latitude != null && longitude != null) {
            moveCamera(LatLng(latitude.toDouble(), longitude.toDouble()))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.normal_map -> {
            map?.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map?.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map?.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onPoiClick(poi: PointOfInterest?) {
        Log.i("z- poi", "true")
        val poiMarker = map?.addMarker(
            marker(
                poi?.latLng!!,
                poi.name!!
            )
        )
        poiMarker?.showInfoWindow()
    }

}