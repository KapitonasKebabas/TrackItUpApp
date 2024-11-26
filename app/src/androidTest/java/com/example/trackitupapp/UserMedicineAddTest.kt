package com.example.trackitupapp.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackitupapp.R
import com.example.trackitupapp.dataHolder.UserMedicine
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserMedicineAddTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(UserMedicineActivity::class.java)

    companion object {
        @BeforeClass
        @JvmStatic
        fun login() {
            val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

            val username = "test"
            val password = "test"

            onView(withId(R.id.username)).perform(typeText(username), closeSoftKeyboard())
            onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard())

            onView(withId(R.id.btn_login)).perform(click())

            Thread.sleep(3000)

            activityScenario.close()
        }
    }
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
    fun testAddMedicineinvalid() {
        onView(withId(R.id.btn_addUserMedicine)).perform(click())

        onView(withId(R.id.editAmount)).perform(typeText("0"), closeSoftKeyboard())
        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(`is`("android.widget.DatePicker"))).perform(setDate(2024, 4, 8))
        onView(withText("OK")).perform(click())
        //onView(withId(R.id.addSwitch)).perform(click())

        onView(withId(R.id.btn_addUserMedicine)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.editAmount)).check(matches(hasErrorText("Amount must be at least 1")))
        onView(withId(R.id.editExpirationDate)).check(matches(hasErrorText("Expiration date cannot be before current date")))
    }


    @Test
    fun testAddMedicine() {
        val intsize= UserMedicine.getList().size
        onView(withId(R.id.btn_addUserMedicine)).perform(click())

        onView(withId(R.id.editAmount)).perform(typeText("2"), closeSoftKeyboard())
        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(`is`("android.widget.DatePicker"))).perform(setDate(2025, 10, 8))
        onView(withText("OK")).perform(click())
        //onView(withId(R.id.addSwitch)).perform(click())

        onView(withId(R.id.btn_addUserMedicine)).perform(click())

        Thread.sleep(1000)
        Assert.assertEquals(intsize+1, UserMedicine.getList().size)

    }
    @Test
    fun testAddMedicineEmptyfields() {
        onView(withId(R.id.btn_addUserMedicine)).perform(click())

        onView(withId(R.id.btn_addUserMedicine)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.editAmount)).check(matches(hasErrorText("Amount cannot be empty")))
        onView(withId(R.id.editExpirationDate)).check(matches(hasErrorText("Expiration date cannot be empty")))
    }
}
