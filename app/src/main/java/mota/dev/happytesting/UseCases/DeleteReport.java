package mota.dev.happytesting.useCases;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.repositories.implementations.ReportLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ReportRemoteImplementation;

/**
 * Created by Slaush on 25/06/2017.
 */

public class DeleteReport
{
    public Observable<Boolean> delete(final Report report)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer)
            {
                if(report.getId() != -1)
                    deleteOnRemote(report,observer);
                else
                    deleteOnLocal(report,observer);
            }
        };
    }

    private void deleteOnRemote(final Report report, final Observer<? super Boolean> observer)
    {
        ReportRepository repo = new ReportRemoteImplementation();
        repo.delete(report).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception
            {
                deleteOnLocal(report,observer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                observer.onNext(false);
                observer.onComplete();
            }
        });
    }

    private void deleteOnLocal(Report report, final Observer<? super Boolean> observer)
    {
        ReportRepository repo = new ReportLocalImplementation();
        repo.delete(report).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception
            {
                observer.onNext(true);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                observer.onNext(false);
                observer.onComplete();
            }
        });
    }
}
