package mota.dev.happytesting.utils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.concurrent.CountDownLatch;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Slaush on 22/05/2017.
 */

public class RxRequestAdapter<T> implements Response.Listener<T>, Response.ErrorListener{

    private final CountDownLatch mLatch;
    private final Observable<T> mObservable;

    private VolleyError mVolleyError;
    private T mResponse;

    public RxRequestAdapter() {
        mLatch = new CountDownLatch(1);
        mObservable = Observable.create(new ObservableOnSubscribe<T>()
        {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception
            {
                try
                {
                    mLatch.await();
                } catch (InterruptedException ex)
                {
                    mVolleyError = new VolleyError(ex);
                }

                if (mVolleyError != null) {
                    e.onError(mVolleyError);
                } else {
                    e.onNext(mResponse);
                    e.onComplete();
                }
            }
        });
    }

    public Observable<T> getObservable() {
        return mObservable;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        mVolleyError = volleyError;
        mLatch.countDown();
    }

    @Override
    public void onResponse(T t) {
        mResponse = t;
        mLatch.countDown();
    }
}