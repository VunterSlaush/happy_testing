package mota.dev.happytesting.utils;

import android.util.Log;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by user on 07/07/2017.
 */

public class RxHelper
{
    private static final String TAG = RxHelper.class.getSimpleName();

    public static void nextAndComplete(Observer observer, Object result)
    {
        observer.onNext(result);
        observer.onComplete();
    }

    public static Consumer<Throwable> getErrorThrowable(final Observer observer, final Object result)
    {
        return new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                Log.d(TAG,"RX Error:"+throwable.getMessage());
                observer.onNext(result);
                observer.onComplete();
            }
        };
    }

    public static Consumer<JSONObject> getSuccessConsumer(final Observer observer)
    {
        return new Consumer<JSONObject>() {
            @Override
            public void accept(@NonNull JSONObject jsonObject) throws Exception
            {
                    observer.onNext(jsonObject.optBoolean("success"));
                    observer.onComplete();
            }
        };
    }
}
