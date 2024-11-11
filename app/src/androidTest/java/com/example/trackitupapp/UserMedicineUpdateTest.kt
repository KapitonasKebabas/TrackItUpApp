import UserMedicineUpdateTest.Constants.updatedAmount
import UserMedicineUpdateTest.Constants.updatedExpiryDate
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
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserMedicineUpdateTest {

}
