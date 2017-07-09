package mota.dev.happytesting.useCases;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.repositories.implementations.ReportLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ReportRemoteImplementation;
import mota.dev.happytesting.utils.RxHelper;

/**
 * Created by Slaush on 11/06/2017.
 */

public class GetReports
{

    public Observable<List<Report>> fetchReports()
    {
        final ReportRepository repo = new ReportRemoteImplementation();
        final ReportLocalImplementation local = ReportLocalImplementation.getInstance();
        return new Observable<List<Report>>()
        {
            @Override
            protected void subscribeActual(final Observer<? super List<Report>> observer)
            {
                final List<Report> reportes = new ArrayList<>();
                mergeDelayError(repo.getAll(),local.getAll()).subscribe(new Observer<List<Report>>()
                {
                    int nextCount = 0;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Report> reports) {
                        for (Report r: reports)
                        {
                            if(!reportes.contains(r))
                                reportes.add(r);
                        }
                        nextCount++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(nextCount == 0)
                            observer.onError(e);
                        else
                            onComplete();
                    }

                    @Override
                    public void onComplete() {
                        saveReportsToLocal(reportes,observer);
                    }
                });
            }
        };
    }

    private void saveReportsToLocal(final List<Report> reportes, final Observer<? super List<Report>> observer)
    {
        ReportLocalImplementation.getInstance().saveAll(reportes).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                RxHelper.nextAndComplete(observer, reportes);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                RxHelper.nextAndComplete(observer, reportes);
            }
        });
    }


}
