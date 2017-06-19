package mota.dev.happytesting.useCases;

import android.util.Log;

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
import mota.dev.happytesting.repositories.implementations.ReportRemoteImplementation;

/**
 * Created by Slaush on 12/06/2017.
 */

public class ReportDetail // TODO Finish it!
{
    public Observable<Report> getDetails(final int id)
    {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(Observer<? super Report> observer)
            {
                getRemoteReportDetails(id, observer);
            }
        };
    }

    private void getRemoteReportDetails(final int id, final Observer<? super Report> observer)
    {
        ReportRepository repo = new ReportRemoteImplementation();
        repo.get(id).subscribe(new Consumer<Report>() {
            @Override
            public void accept(@NonNull Report app) throws Exception {
                observer.onNext(app);
                //AppRepository localRepo = new AppLocalImplementation();
                //localRepo.modifiy(app).subscribe();
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                //getLocalAppDetails(id,observer);
            }
        });
    }
/*
    private void getLocalAppDetails(int id, final Observer<? super Report> observer)
    {
        AppRepository repo = new ReportLocalImplementation();
        repo.get(id).subscribe(new Consumer<Report>() {
            @Override
            public void accept(@NonNull Report app) throws Exception {
                observer.onNext(app);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(throwable);
            }
        });
    }
*/
}
