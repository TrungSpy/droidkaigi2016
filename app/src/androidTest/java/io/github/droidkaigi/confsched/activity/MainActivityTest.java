package io.github.droidkaigi.confsched.activity;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import io.github.droidkaigi.test.IsolateEnvRule;

/**
 * Created by cattaka on 16/10/03.
 */
public class MainActivityTest {
    @Rule
    public IsolateEnvRule isolateEnvRule = new IsolateEnvRule();

    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void simple_run() {
        MainActivity activity = activityTestRule.launchActivity(null);
        SystemClock.sleep(5000);
    }
}
