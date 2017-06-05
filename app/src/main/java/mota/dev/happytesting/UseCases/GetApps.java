package mota.dev.happytesting.useCases;

import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;

/**
 * Created by Slaush on 30/05/2017.
 */

public class GetApps
{
    private AppRepository localRepository, remoteRepository;
    public GetApps()
    {
        localRepository = new AppLocalImplementation();
        remoteRepository = new AppRemoteImplementation();
    }

    public Observable<List<App>> fetchAllApps()
    {
        return new Observable<List<App>>()
        {
            @Override
            protected void subscribeActual(final Observer<? super List<App>> observer)
            {
                final Observable<List<App>> localApps = localRepository.getAll();
                Observable<List<App>> remoteApps = remoteRepository.getAll();
                remoteApps.subscribe(new Consumer<List<App>>() {
                    @Override
                    public void accept(@NonNull List<App> appsRemote) throws Exception
                    {
                        insertRemoteAppsIfNeedAndSendLocal(appsRemote,localApps, observer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                       insertRemoteAppsIfNeedAndSendLocal(null,localApps, observer);
                    }
                });
            }
        };
    }

    private void insertRemoteAppsIfNeedAndSendLocal(List<App> appsRemote, Observable<List<App>> localApps, final Observer<? super List<App>> observer)
    {
        if(appsRemote != null)
            insertAllAppsToLocal(appsRemote);
        localApps.subscribe(new Consumer<List<App>>() {
            @Override
            public void accept(@NonNull List<App> apps) throws Exception {
                observer.onNext(apps);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(new Throwable("Error Al Consultar las Aplicaciones"));
            }
        });
    }

    private void insertAllAppsToLocal(List<App> apps)
    {
        localRepository.updateApps(apps);
    }

}
