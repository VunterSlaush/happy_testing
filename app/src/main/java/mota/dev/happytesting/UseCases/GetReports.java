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
        final ReportLocalImplementation localRepo = ReportLocalImplementation.getInstance();

        return new Observable<List<Report>>() {
            @Override
            protected void subscribeActual(final Observer<? super List<Report>> observer) {
                repo.getAll().subscribe(new Consumer<List<Report>>()
                {
                    @Override
                    public void accept(@NonNull List<Report> ReportsRemote) throws Exception
                    {
                        saveReportsToLocal(ReportsRemote, observer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        saveReportsToLocal(null, observer);
                    }
                });
            }
        };
    }



    private void saveReportsToLocal(final List<Report> reportes, final Observer<? super List<Report>> observer)
    {
        ReportLocalImplementation.getInstance().deleteItemsIfNeeded(reportes);
        ReportLocalImplementation.getInstance().saveAll(reportes).subscribe(new Consumer<Boolean>()
        {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception
            {
                //RxHelper.nextAndComplete(observer, reportes);
                returnLocalReports(observer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                //RxHelper.nextAndComplete(observer, reportes);
                returnLocalReports(observer);
            }
        });
    }

    private void returnLocalReports(final Observer<? super List<Report>> observer)
    {
        ReportLocalImplementation.getInstance().getAll().subscribe(new Consumer<List<Report>>()
        {
            @Override
            public void accept(@NonNull List<Report> reports) throws Exception {
                observer.onNext(reports);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(new Throwable("Error Al Consultar los Reportes"));
            }
        });
    }


}
