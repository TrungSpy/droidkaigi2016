package io.github.droidkaigi.confsched.activity;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.test.IsolateEnvRule;

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
    }
}
