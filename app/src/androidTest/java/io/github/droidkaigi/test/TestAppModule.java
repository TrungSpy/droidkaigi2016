package io.github.droidkaigi.test;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.intent.IntentStubberRegistry;
import android.test.RenamingDelegatingContext;

import com.github.gfx.android.orma.OrmaDatabaseBuilderBase;
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
    private RenamingDelegatingContext rdContext;

    private Tracker tracker;
    private SharedPreferences sharedPreferences;
    private OrmaDatabase ormaDatabase;
    private DroidKaigiClient droidKaigiClient;

    public TestAppModule(Application app) {
        super(app);
        context = app;
        rdContext = new RenamingDelegatingContext(context, "test_");

        GoogleAnalytics ga = GoogleAnalytics.getInstance(context);
        ga.setDryRun(true);
        tracker = ga.newTracker(BuildConfig.GA_TRACKING_ID);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableExceptionReporting(true);

        sharedPreferences = context.getSharedPreferences(TEST_SHARED_PREF_NAME, Context.MODE_PRIVATE);
        ormaDatabase = super.provideOrmaDatabase(rdContext);

        AssetManager assets = InstrumentationRegistry.getContext().getAssets();
        droidKaigiClient = new TestDroidKaigiClient(assets);
    }

    public void reset() {
        sharedPreferences.edit().clear().apply();
        rdContext.deleteDatabase(OrmaDatabaseBuilderBase.getDefaultDatabaseName(context));
    }

    public void shutdown() {
    }

    @Override
    public Tracker provideGoogleAnalyticsTracker(Context context) {
        return tracker;
    }

    @Override
    public SharedPreferences provideSharedPreferences(Context context) {
        return sharedPreferences;
    }

    @Override
    public OrmaDatabase provideOrmaDatabase(Context context) {
        return ormaDatabase;
    }

    @Override
    public DroidKaigiClient provideDroidKaigiClient(OkHttpClient client) {
        return droidKaigiClient;
    }
}
