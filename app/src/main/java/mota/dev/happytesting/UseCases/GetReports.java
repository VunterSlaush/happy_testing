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

/**
 * Created by Slaush on 11/06/2017.
 */

public class GetReports
{

    public Observable<List<Report>> fetchReports()
    {
        final ReportRepository repo = new ReportRemoteImplementation();
        final ReportRepository repo2 = new ReportLocalImplementation();
        return new Observable<List<Report>>()
        {
            @Override
            protected void subscribeActual(final Observer<? super List<Report>> observer)
            {
                final List<Report> reportes = new ArrayList<>();
                concat(repo.getAll(),repo2.getAll()).subscribe(new Observer<List<Report>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Report> reports) {
                        Log.d("MOTA--->","ON NEXT REPORTES:"+reports.toString());
                        for (Report r: reports)
                        {
                            Log.d("MOTA--->","ON NEXT REPORTES:"+r.getName());
                            if(!reportes.contains(r))
                                reportes.add(r);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onNext(reportes);
                        observer.onComplete();
                    }
                });
            }
        };
    }

}
