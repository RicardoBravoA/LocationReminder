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

class MapLocationFragment : BaseMapFragment(), GoogleMap.OnPoiClickListener,
    GoogleMap.OnMapLongClickListener {

    //Use Koin to get the view model of the SaveReminder
    override val viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentMapLocationBinding
    private var poiList: LinkedHashMap<String, PointOfInterest> = linkedMapOf()

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
            poiList[poi.latLng.toString()] = poi
        }
        confirm(poi)
    }

    override fun onMapLongClick(latLng: LatLng?) {
        val poi = PointOfInterest(
            latLng,
            getString(R.string.custom_location),
            getString(R.string.custom_location)
        )
        poiList[poi.latLng.toString()] = poi
        confirm(poi)
    }

    private fun confirm(poi: PointOfInterest?) {
        val poiMarker = map?.addMarker(
            marker(
                poi?.latLng!!,
                poi.name!!
            )
        )
        poiMarker?.showInfoWindow()

        requireContext().showAlertDialog(
            requireContext().getString(R.string.want_poi), poi?.latLng.toString(), ::positiveClick
        )
    }

    private fun positiveClick(key: String) {
        val poi = poiList[key]
        viewModel.addSelectedPOI(poi)
        back()
    }

    private fun back() {
        findNavController().popBackStack()
    }

}