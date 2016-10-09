package io.github.droidkaigi.confsched.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.fragment.SessionsFragment;
import io.github.droidkaigi.test.IsolateEnvRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

/**
 * Created by cattaka on 16/10/09.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public IsolateEnvRule isolateEnvRule = new IsolateEnvRule();

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void check_start_activity() {
        MainActivity activity = activityTestRule.launchActivity(null);

        assertThat(
                "MainActivity is running",
                activity.isFinishing(),
                Matchers.is(false)
        );

        View recyclerView;
        {   // 少々強引だがRecyclerViewのインスタンスを取得する
            ViewPager viewPager = (ViewPager) activity.findViewById(R.id.view_pager);
            FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) viewPager.getAdapter();
            Fragment sessionsTabFragment = adapter.getItem(0);
            recyclerView = sessionsTabFragment.getView();
        }

        Instrumentation.ActivityMonitor monitor = new Instrumentation.ActivityMonitor(SessionDetailActivity.class.getCanonicalName(), null, false);
        InstrumentationRegistry.getInstrumentation().addMonitor(monitor);
        try {
            onView(Matchers.equalTo(recyclerView))
                    .perform(RecyclerViewActions.scrollToPosition(5));
            onView(Matchers.equalTo(recyclerView))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

            Activity nextActivity = monitor.waitForActivityWithTimeout(5000);
            assertThat(nextActivity, Matchers.is(Matchers.notNullValue()));
            nextActivity.finish();
        } finally {
            InstrumentationRegistry.getInstrumentation().removeMonitor(monitor);
        }
    }
}
