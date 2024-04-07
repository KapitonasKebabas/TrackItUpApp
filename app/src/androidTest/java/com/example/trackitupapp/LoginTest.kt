package com.example.trackitupapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.trackitupapp.activities.LoginActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


import androidx.test.ext.junit.runners.AndroidJUnit4
@RunWith(AndroidJUnit4::class)
class LoginTest {
    @Test
    fun testLoginButton() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Aktyvuoti LoginActivity
        onView(withId(R.id.btn_login)).perform(click())

        // Palaukite, kol bus baigtas prisijungimo veiksmas
        onView(withId(R.id.tv_token)).check(matches(withText("Wrong credentials")))

        activityScenario.close()
    }


    @Test
    fun testValidLoginCredentials() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        val username = "test"
        val password = "test"


        onView(withId(R.id.username)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard())


        onView(withId(R.id.btn_login)).perform(click())
        Thread.sleep(3000)


        onView(withId(R.id.username)).check(doesNotExist())

        activityScenario.close()

    }

    @Test
    fun testInvalidLoginCredentials() {

        val username = "testno"
        val password = "testno"

        // Aktyvuoti LoginActivity
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Įvesti neteisingą vartotojo vardą ir slaptažodį
        onView(withId(R.id.username)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard())

        // Paspausti prisijungimo mygtuką
        onView(withId(R.id.btn_login)).perform(click())
        Thread.sleep(3000)
        // Patikrinti, ar buvo parodyta klaidos žinutė
        onView(withId(R.id.tv_token)).check(matches(withText("Wrong credentials")))
        activityScenario.close()
    }
}