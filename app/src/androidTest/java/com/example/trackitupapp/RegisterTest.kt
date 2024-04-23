package com.example.trackitupapp;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.trackitupapp.activities.LoginActivity

import com.example.trackitupapp.activities.RegisterActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4::class)
class RegisterTest {
    @Test
    fun testEmptyFields() {
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)

        onView(withId(R.id.et_firstName_registerAct)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.et_lastName_registerAct)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.et_username_registerAct)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.et_password_registerAct)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.et_email_registerAct)).perform(typeText(""), closeSoftKeyboard())

        onView(withId(R.id.btn_reg_registerAct)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.tv_emailError)).check(matches(withText("This field may not be blank.\n")))
        onView(withId(R.id.tv_pswError)).check(matches(withText("This field may not be blank.\n")))
        onView(withId(R.id.tv_userError)).check(matches(withText("This field may not be blank.\n")))

        activityScenario.close()
    }

    @Test
    fun testValidRegistration() {

        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.btn_regActivity)).perform(click())

        Thread.sleep(1000)

        val firstName = "Johns"
        val lastName = "Does"
        val username = "Johndoes"
        val password = "Johndoes123!"
        val email = "johndoes@example.com"

        onView(withId(R.id.et_firstName_registerAct)).perform(typeText(firstName), closeSoftKeyboard())
        onView(withId(R.id.et_lastName_registerAct)).perform(typeText(lastName), closeSoftKeyboard())
        onView(withId(R.id.et_username_registerAct)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.et_password_registerAct)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.et_email_registerAct)).perform(typeText(email), closeSoftKeyboard())

        onView(withId(R.id.btn_reg_registerAct)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.btn_reg_registerAct)).check(ViewAssertions.doesNotExist())
        activityScenario.close()

    }

    @Test
    fun testEmptyUsername() {
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)

        val firstName = "Johns"
        val lastName = "Does"
        val password = "tests"
        val email = "johndoes@example.com"

        onView(withId(R.id.et_firstName_registerAct)).perform(typeText(firstName), closeSoftKeyboard())
        onView(withId(R.id.et_lastName_registerAct)).perform(typeText(lastName), closeSoftKeyboard())
        onView(withId(R.id.et_username_registerAct)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.et_password_registerAct)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.et_email_registerAct)).perform(typeText(email), closeSoftKeyboard())

        onView(withId(R.id.btn_reg_registerAct)).perform(click())
        Thread.sleep(3000)

        onView(withId(R.id.tv_userError)).check(matches(withText("This field may not be blank.\n")))

        activityScenario.close()
    }
    @Test
    fun testEmptyPassword() {
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)

        val firstName = "Johns"
        val lastName = "Does"
        val username = "johndoes"
        val email = "johndoes@example.com"

        onView(withId(R.id.et_firstName_registerAct)).perform(typeText(firstName), closeSoftKeyboard())
        onView(withId(R.id.et_lastName_registerAct)).perform(typeText(lastName), closeSoftKeyboard())
        onView(withId(R.id.et_username_registerAct)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.et_password_registerAct)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.et_email_registerAct)).perform(typeText(email), closeSoftKeyboard())

        onView(withId(R.id.btn_reg_registerAct)).perform(click())
        Thread.sleep(3000)

        onView(withId(R.id.tv_pswError)).check(matches(withText("This field may not be blank.\n")))

        activityScenario.close()
    }
    @Test
    fun testEmptyEmail() {
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)

        val firstName = "Johns"
        val lastName = "Does"
        val username = "johndoes"
        val password = "password123"

        onView(withId(R.id.et_firstName_registerAct)).perform(typeText(firstName), closeSoftKeyboard())
        onView(withId(R.id.et_lastName_registerAct)).perform(typeText(lastName), closeSoftKeyboard())
        onView(withId(R.id.et_username_registerAct)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.et_password_registerAct)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.et_email_registerAct)).perform(typeText(""), closeSoftKeyboard())

        onView(withId(R.id.btn_reg_registerAct)).perform(click())
        Thread.sleep(3000)

        onView(withId(R.id.tv_emailError)).check(matches(withText("This field may not be blank.\n")))

        activityScenario.close()
    }
}
