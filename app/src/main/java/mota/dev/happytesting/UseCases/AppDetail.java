package mota.dev.happytesting.useCases;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;

/**
 * Created by Slaush on 12/06/2017.
 */

public class AppDetail
{
    public Observable<App> getAppDetails(final int id)
    {
        return new Observable<App>() {
            @Override
            protected void subscribeActual(Observer<? super App> observer)
            {
                getLocalAppDetails(id,observer);
            }
        };
    }

    private void getLocalAppDetails(int id, final Observer<? super App> observer)
    {
        AppRepository repo = new AppLocalImplementation();
        repo.get(id).subscribe(new Consumer<App>() {
            @Override
            public void accept(@NonNull App app) throws Exception {
                observer.onNext(app);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(throwable);
            }
        });
    }

}
