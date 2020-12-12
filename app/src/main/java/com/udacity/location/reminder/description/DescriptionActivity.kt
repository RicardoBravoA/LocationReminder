package com.udacity.location.reminder.description

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.udacity.location.reminder.R
import com.udacity.location.reminder.databinding.ActivityReminderDescriptionBinding
import com.udacity.location.reminder.list.ReminderDataItem
import com.udacity.location.reminder.util.Constant
import kotlin.random.Random

class DescriptionActivity : AppCompatActivity(), OnMapReadyCallback {

    var map: GoogleMap? = null
    private lateinit var binding: ActivityReminderDescriptionBinding
    private var reminderDataItem: ReminderDataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminder_description)
        binding.lifecycleOwner = this

        reminderDataItem = intent?.extras?.getParcelable(Constant.DATA)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        reminderDataItem?.let {
            binding.titleTextView.text = it.title
            binding.descriptionTextView.text = it.description
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        reminderDataItem?.let {
            val location =
                LatLng(it.latitude!!, it.longitude!!)
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constant.MAP_ZOOM))
            map?.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(it.location)
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(
                            Random.nextInt(360).toFloat()
                        )
                    )
            )
        }

        map?.uiSettings?.isZoomControlsEnabled = true
    }

    companion object {
        fun newIntent(context: Context, reminderDataItem: ReminderDataItem): Intent {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra(Constant.DATA, reminderDataItem)
            return intent
        }
    }
}