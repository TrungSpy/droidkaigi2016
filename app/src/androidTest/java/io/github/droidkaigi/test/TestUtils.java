package io.github.droidkaigi.test;

import android.support.test.InstrumentationRegistry;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by takao on 2016/10/04.
 */

public class TestUtils {
    public static Fragment pickFragment(FragmentActivity activity, int fragmentId) {
        AtomicReference<Fragment> ref = new AtomicReference<>();
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                ref.set(activity.getSupportFragmentManager().findFragmentById(fragmentId));
            }
        });
        return ref.get();
    }
}
