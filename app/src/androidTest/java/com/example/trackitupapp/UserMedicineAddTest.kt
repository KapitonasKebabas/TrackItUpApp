package com.example.trackitupapp.activities

import android.widget.Spinner
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackitupapp.R
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserMedicineAddTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(UserMedicineActivity::class.java)

    @Test
    fun testAddUserMedicineBtn_click() {
        onView(withId(R.id.btn_addUserMedicine)).perform(click())
        onView(withId(R.id.sp_aprovedMedicine)).check(matches(not(doesNotExist())))

    }

    @Test
    fun testSpinnerPopulated() {
        onView(withId(R.id.btn_addUserMedicine)).perform(click())
        onView(withId(R.id.sp_aprovedMedicine)).check(matches(isDisplayed()))
    }

    @Test
    fun testDatePickerSetup() {
        onView(withId(R.id.btn_addUserMedicine)).perform(click())
        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(`is`("android.widget.DatePicker"))).check(matches(isDisplayed()))
    }

    @Test
    fun testAddMedicine(){
        onView(withId(R.id.btn_addUserMedicine)).perform(click())


        onView(withId(R.id.editAmount)).perform(typeText("10"), closeSoftKeyboard())
        onView(withId(R.id.editExpirationDate)).perform(click())

        onView(withClassName(`is`("android.widget.DatePicker"))).perform(setDate(2024, 4, 8))
        onView(withText("OK")).perform(click())
        onView(withId(R.id.addSwitch)).perform(click())

        onView(withId(R.id.btn_addUserMedicine)).perform(click())
        Thread.sleep(3000)
    }
}
