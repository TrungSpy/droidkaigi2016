package io.github.droidkaigi.test;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.droidkaigi.confsched.api.DroidKaigiClient;
import io.github.droidkaigi.confsched.model.Contributor;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.model.SessionFeedback;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

/**
 * Created by cattaka on 16/10/02.
 */

public class TestDroidKaigiClient extends DroidKaigiClient {
    AssetManager assets;
    Gson gson;

    public TestDroidKaigiClient(AssetManager assets) {
        this.assets = assets;
        this.gson = createGson();
    }

    @Override
    public Observable<List<Session>> getSessions(@NonNull String languageId) {
        Type listType = new TypeToken<ArrayList<Session>>() {}.getType();
        return createObservable("DroidKaigiService/sessions_ja.json", listType);
    }

    @Override
    public Observable<Response<Void>> submitSessionFeedback(SessionFeedback f) {
        return Observable.just(Response.success(null));
    }

    @Override
    public Observable<List<Contributor>> getContributors() {
        Type listType = new TypeToken<ArrayList<Contributor>>() {}.getType();
        return createObservable("GithubService/contributors.json", listType);
    }

    private <T> Observable<T> createObservable(String filename, Type type) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                Reader reader = null;
                try {
                    reader = new InputStreamReader(assets.open(filename));
                    subscriber.onNext(gson.fromJson(reader, type));
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
        });
    }

    private Reader openReader(String filename) throws IOException {
        return new InputStreamReader(assets.open(filename));
    }
}
