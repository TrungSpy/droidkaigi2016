package io.github.droidkaigi.confsched.activity;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by cattaka on 16/10/09.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void check_start_activity() {
        MainActivity activity = activityTestRule.launchActivity(null);

        SystemClock.sleep(5000);
        assertThat(
                "MainActivity is running",
                activity.isFinishing(),
                Matchers.is(false)
        );
    }
}
