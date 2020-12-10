package com.udacity.location.reminder.list

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.udacity.location.reminder.R
import com.udacity.location.reminder.base.BaseFragment
import com.udacity.location.reminder.base.NavigationCommand
import com.udacity.location.reminder.databinding.FragmentRemindersBinding
import com.udacity.location.reminder.util.setDisplayHomeAsUpEnabled
import com.udacity.location.reminder.util.setTitle
import com.udacity.location.reminder.util.setup
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : BaseFragment() {

    override val viewModel: RemindersListViewModel by viewModel()
    private lateinit var binding: FragmentRemindersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRemindersBinding.inflate(inflater)
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(false)
        setTitle(getString(R.string.app_name))

        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.loadReminders() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()

        binding.addButton.setOnClickListener {
            viewModel.navigationCommand.postValue(
                NavigationCommand.To(
                    ReminderListFragmentDirections.toSaveReminder()
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        //load the reminders list on the ui
        viewModel.loadReminders()
    }

    private fun setupRecyclerView() {
        val adapter = RemindersListAdapter {
        }

//        setup the recycler view using the extension function
        binding.reminderRecyclerView.setup(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener {
                        findNavController().navigate(ReminderListFragmentDirections.actionReminderListFragmentToMainFragment())
                    }
                    .addOnFailureListener {
                        Snackbar.make(
                            binding.swipeRefreshLayout,
                            requireActivity().getString(R.string.app_name),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}