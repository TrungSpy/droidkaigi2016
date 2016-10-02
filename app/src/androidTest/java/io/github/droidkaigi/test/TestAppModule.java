package io.github.droidkaigi.test;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.intent.IntentStubberRegistry;
import android.test.RenamingDelegatingContext;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.github.droidkaigi.confsched.BuildConfig;
import io.github.droidkaigi.confsched.activity.ActivityNavigator;
import io.github.droidkaigi.confsched.api.DroidKaigiClient;
import io.github.droidkaigi.confsched.di.AppModule;
import io.github.droidkaigi.confsched.model.OrmaDatabase;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by cattaka on 16/10/02.
 */

public class TestAppModule extends AppModule {
    static final String TEST_SHARED_PREF_NAME = "test_preferences";

    private Context context;

    public TestAppModule(Application app) {
        super(app);
        context = app;
    }

    @Override
    public Tracker provideGoogleAnalyticsTracker(Context context) {
        GoogleAnalytics ga = GoogleAnalytics.getInstance(context);
        ga.setDryRun(true);
        Tracker tracker = ga.newTracker(BuildConfig.GA_TRACKING_ID);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableExceptionReporting(true);
        return tracker;
    }

    @Override
    public SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(TEST_SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public OrmaDatabase provideOrmaDatabase(Context context) {
        RenamingDelegatingContext rdContext = new RenamingDelegatingContext(context, "test_");
        return super.provideOrmaDatabase(rdContext);
    }

    @Override
    public DroidKaigiClient provideDroidKaigiClient(OkHttpClient client) {
        AssetManager assets = InstrumentationRegistry.getContext().getAssets();
        return new TestDroidKaigiClient(assets);
    }
}
