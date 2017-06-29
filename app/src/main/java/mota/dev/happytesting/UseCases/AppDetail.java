package mota.dev.happytesting.useCases;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;
import mota.dev.happytesting.repositories.implementations.ReportLocalImplementation;

/**
 * Created by Slaush on 12/06/2017.
 */

public class AppDetail
{
    public Observable<App> getAppDetails(final int id, final String appName)
    {
        return new Observable<App>() {
            @Override
            protected void subscribeActual(Observer<? super App> observer)
            {
                getRemoteAppDetails(id,appName, observer);
            }
        };
    }

    private void getRemoteAppDetails(final int id, final String app_name, final Observer<? super App> observer)
    {
        AppRepository repo = AppRemoteImplementation.getInstance();
        repo.get(id, null).subscribe(new Consumer<App>() {
            @Override
            public void accept(@NonNull App app) throws Exception {;
                getLocalAppReports(app,observer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                getLocalAppDetails(id,app_name,observer);
            }
        });
    }

    private void getLocalAppReports(final App app, final Observer<? super App> observer)
    {
        ReportRepository repo = new ReportLocalImplementation();
        repo.getAppReports(app).subscribe(new Consumer<List<Report>>() {
            @Override
            public void accept(@NonNull List<Report> reports) throws Exception
            {
                for (Report r : reports)
                {
                    if(!app.getReports().contains(r))
                        app.addReport(r);
                }
                AppLocalImplementation.getInstance().modifiy(app).subscribe();
                observer.onNext(app);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(new Throwable("no se consiguieron los reportes de esta aplicacion"));
            }
        });
    }

    private void getLocalAppDetails(int id,String name, final Observer<? super App> observer)
    {
        AppRepository repo = AppLocalImplementation.getInstance();
        repo.get(id, name).subscribe(new Consumer<App>() {
            @Override
            public void accept(@NonNull App app) throws Exception {
                getLocalAppReports(app,observer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(throwable);
            }
        });
    }

}
