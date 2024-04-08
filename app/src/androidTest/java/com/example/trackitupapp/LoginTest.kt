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
    fun testEmptyUsername() {
        // Aktyvuoti LoginActivity
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Palikti vartotojo vardo lauką tuščią, tačiau įvesti slaptažodį
        onView(withId(R.id.username)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("test"), closeSoftKeyboard())

        // Paspausti prisijungimo mygtuką
        onView(withId(R.id.btn_login)).perform(click())

        // Patikrinti, ar buvo parodyta klaidos žinutė dėl tuščio vartotojo vardo
        onView(withId(R.id.tv_token)).check(matches(withText("Please enter username")))
        activityScenario.close()
    }

    @Test
    fun testEmptyPassword() {
        // Aktyvuoti LoginActivity
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Palikti slaptažodžio lauką tuščią, tačiau įvesti vartotojo vardą
        onView(withId(R.id.username)).perform(typeText("test"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard())

        // Paspausti prisijungimo mygtuką
        onView(withId(R.id.btn_login)).perform(click())

        // Patikrinti, ar buvo parodyta klaidos žinutė dėl tuščio slaptažodžio
        onView(withId(R.id.tv_token)).check(matches(withText("Please enter password")))
        activityScenario.close()
    }

    @Test
    fun testBothEmpty() {
        // Aktyvuoti LoginActivity
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // Palikti abu laukus tuščius
        onView(withId(R.id.username)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard())

        // Paspausti prisijungimo mygtuką
        onView(withId(R.id.btn_login)).perform(click())

        // Patikrinti, ar buvo parodyta klaidos žinutė dėl abiejų tuščių laukų
        onView(withId(R.id.tv_token)).check(matches(withText("Please enter username and password")))
        activityScenario.close()
    }

    @Test
    fun testValidLoginCredentials() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        val username = "test"
        val password = "test"

        // Įvesti vartotojo vardą ir slaptažodį
        onView(withId(R.id.username)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard())

        // Paspausti prisijungimo mygtuką
        onView(withId(R.id.btn_login)).perform(click())
        Thread.sleep(3000)

        // Patikrinti, ar LoginActivity yra uždarytas
        onView(withId(R.id.username)).check(doesNotExist())

        // Uždaryti LoginActivity
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