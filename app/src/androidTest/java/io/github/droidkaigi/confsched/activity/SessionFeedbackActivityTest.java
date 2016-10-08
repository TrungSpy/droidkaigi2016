package io.github.droidkaigi.confsched.activity;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.test.CustomViewAction;
import io.github.droidkaigi.test.IsolateEnvRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static io.github.droidkaigi.test.CustomViewAction.setProgress;

/**
 * Created by takao on 2016/10/04.
 */
public class SessionFeedbackActivityTest {
    @Rule
    public IsolateEnvRule isolateEnvRule = new IsolateEnvRule();

    public ActivityTestRule<SessionFeedbackActivity> activityTestRule = new ActivityTestRule<>(SessionFeedbackActivity.class, false, false);

    @Test
    public void check_request_on_click() {
        Session session;
        {   // 偽物の講演情報を取得する
            List<Session> sessions = isolateEnvRule.appModule.droidKaigiClient.getSessions("ja").toBlocking().first();
            session = sessions.get(0);
        }

        Context targetContext = InstrumentationRegistry.getTargetContext();
        Intent intent = SessionFeedbackActivity.createIntent(targetContext, session);
        SessionFeedbackActivity activity = activityTestRule.launchActivity(intent);

        onView(withId(R.id.relevant_feedback_bar))
                .perform(scrollTo(), setProgress(1));
        onView(withId(R.id.as_expected_feedback_bar))
                .perform(scrollTo(), setProgress(2));
        onView(withId(R.id.difficulty_feedback_bar))
                .perform(scrollTo(), setProgress(3));
        onView(withId(R.id.knowledgeable_feedback_bar))
                .perform(scrollTo(), setProgress(4));
        onView(withId(R.id.other_comments_feedback_text))
                .perform(
                        scrollTo(),
                        replaceText("Hogehoge Fugafuga"));
        onView(withId(R.id.submit_feedback_button))
                .perform(scrollTo(), click());
    }
}
