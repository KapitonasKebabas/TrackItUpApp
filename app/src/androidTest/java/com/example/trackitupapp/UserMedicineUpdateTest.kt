import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackitupapp.R
import com.example.trackitupapp.activities.UserMedicineActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserMedicineUpdateTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(UserMedicineActivity::class.java)

    @Test
    fun testItemPickerSetup() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
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

        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(CoreMatchers.`is`("android.widget.DatePicker"))).check(matches(isDisplayed()))
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

        onView(withId(R.id.editAmount)).perform(clearText(), typeText("999"), closeSoftKeyboard())

        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(CoreMatchers.`is`("android.widget.DatePicker"))).perform(setDate(2024, 4, 13))
        onView(withText("OK")).perform(click())

        onView(withText("Save")).perform(click())
    }

    @Test
    fun testUpdateWithShare() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    3,
                    click()
                )
            )

        onView(withId(R.id.editAmount)).perform(clearText(), typeText("20"), closeSoftKeyboard())

        onView(withId(R.id.editExpirationDate)).perform(click())
        onView(withClassName(CoreMatchers.`is`("android.widget.DatePicker"))).perform(setDate(2024, 4, 10))
        onView(withText("OK")).perform(click())

        onView(withId(R.id.editSwitch)).perform(click())

        onView(withText("Save")).perform(click())
    }

    @Test
    fun testCancel() {
        onView(withId(R.id.rv_medicineHolder))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    4,
                    click()
                )
            )

        onView(withText("Cancel")).perform(click())
    }
}
