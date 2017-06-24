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
 * Created by Slaush on 23/06/2017.
 */

public class DeleteApp
{

    public Observable<Boolean> delete(final App app)
    {
        return new Observable<Boolean>()
        {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer)
            {
                if(app.getId() >= 0)
                    deleteOnServer(app,observer);
                else
                    deleteLocal(app, observer);
            }
        };
    }

    private void deleteOnServer(final App app, final Observer<? super Boolean> observer)
    {
        AppRepository remote = new AppRemoteImplementation();
        remote.delete(app).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean result) throws Exception
            {
                if(result)
                {
                    deleteLocal(app, observer);
                }
                else
                {
                    observer.onNext(false);
                    observer.onComplete();
                }
            }
        });
    }

    private void deleteLocal(final App app, final Observer<? super Boolean> observer)
    {
        AppRepository local = new AppLocalImplementation();
        local.delete(app).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean result) throws Exception
            {
                observer.onNext(result);
                observer.onComplete();
            }
        });
    }
}
