package com.udacity.location.reminder.map

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.location.reminder.R
import com.udacity.location.reminder.databinding.FragmentMapLocationBinding
import com.udacity.location.reminder.save.SaveReminderViewModel
import com.udacity.location.reminder.util.setDisplayHomeAsUpEnabled
import com.udacity.location.reminder.util.showAlertDialog
import org.koin.android.ext.android.inject

class MapLocationFragment : BaseMapFragment(), GoogleMap.OnPoiClickListener,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

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

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)

        map?.setOnPoiClickListener(this)
        map?.setOnMapLongClickListener(this)
        map?.setOnMarkerClickListener(this)
    }

    override fun locationCallback(): LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            locationResult?.let { result ->
                location = result.locations[locationResult.locations.size - 1]

                location?.let {
                    moveCamera(LatLng(it.latitude, it.longitude))
                }
            }
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
        poi?.let {
            confirm(addMarker(it))
        }
    }

    override fun onMapLongClick(latLng: LatLng?) {
        val poi = PointOfInterest(
            latLng,
            getString(R.string.custom_location),
            getString(R.string.custom_location)
        )
        confirm(addMarker(poi))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {

            val poi = markerList[(it.tag as Int)]

            requireContext().showAlertDialog(
                requireContext().getString(R.string.want_poi),
                poi.tag as Int,
                ::positiveClick
            )
        }
        return false
    }

    private fun confirm(marker: Marker?) {
        marker?.let {
            requireContext().showAlertDialog(
                requireContext().getString(R.string.want_poi), it.tag as Int, ::positiveClick
            )
        }
    }

    private fun positiveClick(key: Int) {
        val poi = markerList[key]
        viewModel.addSelectedMarker(poi)
        back()
    }

    private fun back() {
        findNavController().popBackStack()
    }

}