package com.udacity.location.reminder.map

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.location.reminder.R
import com.udacity.location.reminder.databinding.FragmentMapLocationBinding
import com.udacity.location.reminder.save.SaveReminderViewModel
import com.udacity.location.reminder.util.setDisplayHomeAsUpEnabled
import com.udacity.location.reminder.util.showAlertDialog
import org.koin.android.ext.android.inject

class MapLocationFragment : BaseMapFragment(), GoogleMap.OnPoiClickListener {

    //Use Koin to get the view model of the SaveReminder
    override val viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentMapLocationBinding
    private var poi: PointOfInterest? = null

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

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)

        map?.setOnPoiClickListener(this)
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
        android.R.id.home -> {
            back()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onPoiClick(poi: PointOfInterest?) {
        val poiMarker = map?.addMarker(
            marker(
                poi?.latLng!!,
                poi.name!!
            )
        )
        poiMarker?.showInfoWindow()
        this.poi = poi

        requireContext().showAlertDialog(
            requireContext().getString(R.string.want_poi), ::positiveClick, ::negativeClick
        )
    }

    private fun positiveClick() {
        viewModel.selectedPOI.value = poi
        back()
    }

    private fun negativeClick() {
        poi = null
    }

    private fun back() {
        findNavController().popBackStack()
    }

}