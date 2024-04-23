
import UserMedicineUpdateTest.Constants.updatedAmount
import UserMedicineUpdateTest.Constants.updatedAmountNegative
import UserMedicineUpdateTest.Constants.updatedExpiryDate
import UserMedicineUpdateTest.Constants.updatedExpiryDateNegative
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackitupapp.R
import com.example.trackitupapp.activities.LoginActivity
import com.example.trackitupapp.activities.UserMedicineActivity
import com.example.trackitupapp.constants.Constants.DATE_FORMAT
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserMedicineUpdateTest {
    object Constants {
        val updatedAmount = 111
        val updatedExpiryDate = DATE_FORMAT.format(DATE_FORMAT.parse("2024-04-10"))
        val updatedAmountNegative = -111
        val updatedExpiryDateNegative = DATE_FORMAT.format(DATE_FORMAT.parse("2020-04-10"))
    }

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(UserMedicineActivity::class.java)

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
    fun testItemPickerSetup() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )
    }
    @Test
    fun testDatePickerSetup() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )

        Thread.sleep(1000)

        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(CoreMatchers.`is`("android.widget.DatePicker"))).check(matches(isDisplayed()))
    }

    @Test
    fun testCancel() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    3,
                    click()
                )
            )

        Thread.sleep(1000)

        onView(withText("Cancel")).perform(click())
    }

    @Test
    fun testUpdate() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    click()
                )
            )

        Thread.sleep(1000)

        onView(withId(R.id.editAmount))
            .perform(clearText(), typeText(updatedAmount.toString()), closeSoftKeyboard())

        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(CoreMatchers.`is`("android.widget.DatePicker")))
            .perform(setDate(2024, 4, 10))
        onView(withText("OK")).perform(click())

        onView(withText("Save")).perform(click())

        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )

        Thread.sleep(1000)

        onView(withId(R.id.editAmount)).check(matches(withText(updatedAmount.toString())))
        onView(withId(R.id.editExpirationDate)).check(matches(withText(updatedExpiryDate)))
    }

    @Test
    fun testUpdateWithShare() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    click()
                )
            )

        Thread.sleep(1000)

        onView(withId(R.id.editAmount))
            .perform(clearText(), typeText(updatedAmount.toString()), closeSoftKeyboard())

        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(CoreMatchers.`is`("android.widget.DatePicker")))
            .perform(setDate(2024, 4, 10))
        onView(withText("OK")).perform(click())

        onView(withId(R.id.editSwitch)).perform(click())

        onView(withText("Save")).perform(click())

        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    2,
                    click()
                )
            )

        Thread.sleep(1000)

        onView(withId(R.id.editAmount)).check(matches(withText(updatedAmount.toString())))
        onView(withId(R.id.editExpirationDate)).check(matches(withText(updatedExpiryDate)))
    }

    @Test
    fun testUpdateNegativeValues() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )

        Thread.sleep(1000)

        onView(withId(R.id.editAmount))
            .perform(clearText(), typeText(updatedAmountNegative.toString()), closeSoftKeyboard())

        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(CoreMatchers.`is`("android.widget.DatePicker")))
            .perform(setDate(2020, 4, 10))
        onView(withText("OK")).perform(click())

        onView(withText("Save")).perform(click())

        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    3,
                    click()
                )
            )

        Thread.sleep(1000)

        onView(withId(R.id.editAmount)).check(matches(not(withText(updatedAmountNegative.toString()))))
        onView(withId(R.id.editExpirationDate)).check(matches(not(withText(updatedExpiryDateNegative))))
    }

}
