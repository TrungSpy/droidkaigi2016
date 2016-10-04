package io.github.droidkaigi.confsched.activity;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;

import java.util.List;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.api.DroidKaigiClient;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.model.SessionFeedback;
import io.github.droidkaigi.test.IsolateEnvRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static io.github.droidkaigi.test.CustomViewAction.setProgress;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

/**
 * Created by takao on 2016/10/04.
 */
public class SessionFeedbackActivityTest {
    @Rule
    public IsolateEnvRule isolateEnvRule = new IsolateEnvRule();

    public ActivityTestRule<SessionFeedbackActivity> activityTestRule = new ActivityTestRule<>(SessionFeedbackActivity.class, false, false);

    @Test
    public void check_request_on_click() {
        DroidKaigiClient droidKaigiClient = Mockito.mock(DroidKaigiClient.class, new ForwardsInvocations(isolateEnvRule.appModule.droidKaigiClient));
        isolateEnvRule.appModule.droidKaigiClient = droidKaigiClient;

        List<Session> sessions = isolateEnvRule.appModule.droidKaigiClient.getSessions("ja").toBlocking().first();
        Session session = sessions.get(0);

        Context targetContext = InstrumentationRegistry.getTargetContext();
        Intent intent = SessionFeedbackActivity.createIntent(targetContext, session);
        SessionFeedbackActivity activity = activityTestRule.launchActivity(intent);

        onView(withId(R.id.relevant_feedback_bar)).perform(scrollTo(), setProgress(1));
        onView(withId(R.id.as_expected_feedback_bar)).perform(scrollTo(), setProgress(2));
        onView(withId(R.id.difficulty_feedback_bar)).perform(scrollTo(), setProgress(3));
        onView(withId(R.id.knowledgeable_feedback_bar)).perform(scrollTo(), setProgress(4));
        onView(withId(R.id.other_comments_feedback_text)).perform(scrollTo(), replaceText("Hogehoge Fugafuga"));
        onView(withId(R.id.submit_feedback_button)).perform(scrollTo(), click());

        ArgumentCaptor<SessionFeedback> captor = ArgumentCaptor.forClass(SessionFeedback.class);
        verify(droidKaigiClient).submitSessionFeedback(captor.capture());
        SessionFeedback value = captor.getValue();
        assertThat(value.relevancy, is(1));
        assertThat(value.asExpected, is(2));
        assertThat(value.difficulty, is(3));
        assertThat(value.knowledgeable, is(4));
        assertThat(value.comment, is("Hogehoge Fugafuga"));
    }

}