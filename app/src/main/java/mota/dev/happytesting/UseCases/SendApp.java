package mota.dev.happytesting.useCases;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;

/**
 * Created by Slaush on 03/07/2017.
 */

public class SendApp {
    private static SendApp instance;

    private SendApp() {
    }

    public static SendApp getInstance() {
        if (instance == null)
            instance = new SendApp();
        return instance;
    }

    public Observable<App> send(final App app) {
        return new Observable<App>() {
            @Override
            protected void subscribeActual(final Observer<? super App> observer) {
                AppRemoteImplementation
                        .getInstance()
                        .create(app.getName(), app.getModificar())
                        .subscribe(new Consumer<App>() {
                            @Override
                            public void accept(@NonNull final App serverApp) throws Exception {
                                saveAppOnLocal(serverApp,observer);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                observer.onError(new Throwable("Error al Enviar la Aplicacion"));
                            }
                        });
            }
        };
    }

    private void saveAppOnLocal(final App serverApp, final Observer<? super App> observer)
    {
        AppLocalImplementation.getInstance()
                .modifiy(serverApp)
                .subscribe(new Consumer<App>() {
            @Override
            public void accept(@NonNull App app) throws Exception {
                observer.onNext(app);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onNext(serverApp);
                observer.onComplete();
            }
        });
    }

}
