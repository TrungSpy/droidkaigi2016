package io.github.droidkaigi.test;

import android.support.test.InstrumentationRegistry;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.di.AppComponent;
import io.github.droidkaigi.confsched.di.DaggerAppComponent;

/**
 * Created by cattaka on 16/10/02.
 */

public class IsolateEnvRule implements TestRule {
    public TestAppModule appModule;

    @Override
    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }

    private Statement statement(final Statement base, final Description desc) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }
    private void before() {
        MainApplication application = (MainApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        appModule = new TestAppModule(application);
        appModule.reset();
        AppComponent testAppComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build();
        application.setAppComponent(testAppComponent);
    }
    private void after() {
        appModule.shutdown();
    }
}
