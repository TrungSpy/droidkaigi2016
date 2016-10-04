package io.github.droidkaigi.confsched.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import org.junit.Rule;
import org.junit.Test;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.fragment.SessionsFragment;
import io.github.droidkaigi.test.IsolateEnvRule;
import io.github.droidkaigi.test.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

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

        {
            Fragment fragment = TestUtils.pickFragment(activity, R.id.content_view);
            assertThat(fragment, is(instanceOf(SessionsFragment.class)));
        }

        SessionsFragment sessionsFragment;
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.view_pager);

        FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) viewPager.getAdapter();
        Fragment sessionsTabFragment = adapter.getItem(0);
        onView(equalTo(sessionsTabFragment.getView())).perform(scrollToPosition(5));

        Instrumentation.ActivityMonitor monitor = new Instrumentation.ActivityMonitor(SessionDetailActivity.class.getCanonicalName(), null, false);
        InstrumentationRegistry.getInstrumentation().addMonitor(monitor);
        try {
            onView(equalTo(sessionsTabFragment.getView())).perform(actionOnItemAtPosition(5, click()));

            Activity nextActivity = monitor.waitForActivityWithTimeout(5000);
            assertThat(nextActivity, is(notNullValue()));
            nextActivity.finish();
        } finally {
            InstrumentationRegistry.getInstrumentation().removeMonitor(monitor);
        }
    }
}
