package com.udacity.location.reminder.save

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.udacity.location.reminder.R
import com.udacity.location.reminder.base.BaseFragment
import com.udacity.location.reminder.base.NavigationCommand
import com.udacity.location.reminder.databinding.FragmentSaveReminderBinding
import com.udacity.location.reminder.util.setDisplayHomeAsUpEnabled
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
            val title = viewModel.reminderTitle.value
            val description = viewModel.reminderDescription
            val poi = viewModel.selectedPOI.value

//            TODO: use the user entered reminder details to:
//             1) add a geofencing request
//             2) save the reminder to the local db

            viewModel.validate(
                binding.titleEditText.text.toString(),
                binding.descriptionEditText.text.toString()
            )
        }

        // Show data
        viewModel.selectedPOI.observe(viewLifecycleOwner, {
            binding.selectedLocationTextView.text = it?.name
        })

        viewModel.reminderTitle.observe(viewLifecycleOwner, { singleEvent ->
            singleEvent?.getContentIfNotHandled()?.let {
                binding.titleEditText.setText(it)
            }
        })

        viewModel.reminderDescription.observe(viewLifecycleOwner, { singleEvent ->
            singleEvent?.getContentIfNotHandled()?.let {
                binding.descriptionEditText.setText(it)
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