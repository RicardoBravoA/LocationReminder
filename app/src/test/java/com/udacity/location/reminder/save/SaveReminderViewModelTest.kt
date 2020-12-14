package com.udacity.location.reminder.save

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.location.reminder.FakeResourceProvider
import com.udacity.location.reminder.MainCoroutineRule
import com.udacity.location.reminder.data.FakeReminderDataSource
import com.udacity.location.reminder.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@ExperimentalCoroutinesApi
class SaveReminderViewModelTest : KoinTest {

    private lateinit var viewModel: SaveReminderViewModel
    private lateinit var dataSource: FakeReminderDataSource
    private lateinit var resourceProvider: FakeResourceProvider

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() = runBlockingTest {
        koinApplication {
            stopKoin()
        }
        dataSource = FakeReminderDataSource()
        resourceProvider = FakeResourceProvider(ApplicationProvider.getApplicationContext())
        viewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            dataSource,
            resourceProvider
        )
    }

    @Test
    fun validateSelectPOITest() = runBlocking {
        viewModel.addSelectedPOI(
            PointOfInterest(
                LatLng(40.7536, -73.9831),
                "Googleplex",
                "Googleplex"
            )
        )
        val value = viewModel.selectedPOI.getOrAwaitValue()
        MatcherAssert.assertThat(value, CoreMatchers.not(CoreMatchers.nullValue()))
    }

    @Test
    fun validateShowLoadingTest() = runBlocking {
        mainCoroutineRule.pauseDispatcher()

        viewModel.addSelectedPOI(
            PointOfInterest(
                LatLng(40.7536, -73.9831),
                "Googleplex",
                "Googleplex"
            )
        )
        viewModel.validate("Title", "Description")

        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

}