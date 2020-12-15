package com.udacity.location.reminder.save

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import com.udacity.location.reminder.R
import com.udacity.location.reminder.base.BaseFragment
import com.udacity.location.reminder.base.NavigationCommand
import com.udacity.location.reminder.common.GeofenceActivity
import com.udacity.location.reminder.databinding.FragmentSaveReminderBinding
import com.udacity.location.reminder.util.setDisplayHomeAsUpEnabled
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SaveReminderFragment : BaseFragment() {

    override val viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSaveReminderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_save_reminder, container, false)

        setDisplayHomeAsUpEnabled(true)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.selectLocationTextView.setOnClickListener {
            viewModel.navigationCommand.value =
                NavigationCommand.To(SaveReminderFragmentDirections.actionSaveReminderFragmentToMapLocationFragment())
        }

        binding.saveReminder.setOnClickListener {
            viewModel.validate(
                binding.titleEditText.text.toString(),
                binding.descriptionEditText.text.toString()
            )
        }

        // Show data
        viewModel.selectedMarker.observe(viewLifecycleOwner, {
            binding.selectedLocationTextView.text = it?.title
        })

        viewModel.addGeofence.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let {
                viewModel.viewModelScope.launch {
                    (requireActivity() as GeofenceActivity).addGeofences()
                }
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        //make sure to clear the view model after destroy, as it's a single view model.
        viewModel.onClear()
    }
}