package mota.dev.happytesting.useCases;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;

/**
 * Created by Slaush on 24/06/2017.
 */

public class EditEditors
{
    public Observable<Boolean> edit(final App app)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                editOnServer(app, observer);
            }
        };
    }

    private void editOnServer(final App app, final Observer<? super Boolean> observer)
    {
        AppRepository repo = AppRemoteImplementation.getInstance();
        repo.modifiy(app).subscribe(new Consumer<App>() {
            @Override
            public void accept(@NonNull App app) throws Exception
            {
                editOnLocal(app, observer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onNext(false);
                observer.onComplete();
            }
        });
    }

    private void editOnLocal(final App app, final Observer<? super Boolean> observer)
    {
        AppRepository repo = AppLocalImplementation.getInstance();
        repo.modifiy(app).subscribe(new Consumer<App>() {
            @Override
            public void accept(@NonNull App app) throws Exception
            {
                observer.onNext(true);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onNext(false);
                observer.onComplete();
            }
        });
    }
}
