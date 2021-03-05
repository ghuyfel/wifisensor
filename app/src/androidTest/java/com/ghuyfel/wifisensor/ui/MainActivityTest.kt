package com.ghuyfel.wifisensor.ui

import android.Manifest
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ghuyfel.wifisensor.R
import com.ghuyfel.wifisensor.di.AppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UninstallModules(AppModule::class)
@HiltAndroidTest
@MediumTest
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_viewsPermissionNotGranted_PermissionDeniedDialogShown() {
        val scenario = launch(MainActivity::class.java)
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val results = mapOf(
            Manifest.permission.ACCESS_WIFI_STATE to false,
            Manifest.permission.ACCESS_FINE_LOCATION to false
        )

        scenario.onActivity { it.onActivityResult(results) }
        onView(withText(context.getString(R.string.request_permission)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_PermissionDeniedCancelButtonClicked_ButtonsDisabled() {
        val scenario = launch(MainActivity::class.java)
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val results = mapOf(
            Manifest.permission.ACCESS_WIFI_STATE to false,
            Manifest.permission.ACCESS_FINE_LOCATION to false
        )

        scenario.onActivity { it.onActivityResult(results) }

        onView(withText(context.getString(android.R.string.cancel)))
            .perform(click())

        onView(withId(R.id.bt_start))
            .check(matches(not(isEnabled())))

    }

    @Test
    fun test_ViewModelPostsData_TextViewIsUpdated() {
        val testMessage = "This is a test message"
        val expectedMessage = "$testMessage\n\n" //we added this at the end of each data set
        val scenario = launch(MainActivity::class.java)

        scenario.onActivity {
            it.viewModel.readingsLiveData.postValue(testMessage)
        }
        onView(withId(R.id.tv_logs))
            .check(matches(withText(expectedMessage)))
    }

    @Test
    fun test_viewsAreDisplayed() {
        launch(MainActivity::class.java)
        onView(withId(R.id.bt_start)).check(matches(isDisplayed()))
        onView(withId(R.id.bt_stop)).check(matches(isDisplayed()))
    }

}