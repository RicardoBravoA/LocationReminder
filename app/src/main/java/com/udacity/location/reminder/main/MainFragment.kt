package com.udacity.location.reminder.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.udacity.location.reminder.R
import com.udacity.location.reminder.base.BaseFragment
import com.udacity.location.reminder.base.NavigationCommand
import com.udacity.location.reminder.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment() {

    override val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel.authenticationState.observe(viewLifecycleOwner, { authenticationState ->
            if (authenticationState == AuthenticationState.AUTHENTICATED) {
                navigateToReminderListFragment()
            }
        })

        binding.loginButton.setOnClickListener {
            launchSignInFlow()
        }

        return binding.root
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.map)
                .setTheme(R.style.Theme_LocationReminder)
                .setIsSmartLockEnabled(true)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                navigateToReminderListFragment()
            }
        }
    }

    private fun navigateToReminderListFragment() {
        viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                MainFragmentDirections.actionMainFragmentToReminderListFragment()
            )
        )
    }

    companion object {
        const val SIGN_IN_RESULT_CODE = 1000
    }

}