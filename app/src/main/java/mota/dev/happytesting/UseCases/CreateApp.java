package mota.dev.happytesting.useCases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.MainActivity;
import mota.dev.happytesting.managers.ErrorManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;

/**
 * Created by Slaush on 29/05/2017.
 */

public class CreateApp {

    private static CreateApp instance;
    private CreateApp() {}
    public static CreateApp getInstance()
    {
        if(instance == null)
            instance = new CreateApp();
        return instance;
    }


    public Observable<App> createApp(final String name, final List<User> selected)
    {
        return new Observable<App>()
        {
            @Override
            protected void subscribeActual(final Observer<? super App> observer)
            {
                AppLocalImplementation.getInstance()
                        .create(name, selected)
                        .subscribe(new Consumer<App>() {
                            @Override
                            public void accept(@NonNull App app) throws Exception {
                                createAppOnRemote(app, observer);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("MOTA--->","Error>"+throwable.getMessage());
                                observer.onError(new Throwable("No Pudo ser creada la Aplicacion"));
                            }
                        });
            }
        };


    }


    private void createAppOnRemote(final App app, final Observer<? super App> observer)
    {
        SendApp.getInstance().send(app).subscribe(new Consumer<App>()
        {
            @Override
            public void accept(@NonNull App serverApp) throws Exception
            {
                observer.onNext(serverApp);
                observer.onComplete();
            }
        }, new Consumer<Throwable>()
        {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                observer.onNext(app);
                observer.onComplete();
            }
        });
    }

}
