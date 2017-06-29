package mota.dev.happytesting.useCases;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ReportLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ReportRemoteImplementation;

import static io.reactivex.Observable.concat;
import static io.reactivex.Observable.merge;
import static io.reactivex.Observable.mergeDelayError;

/**
 * Created by Slaush on 12/06/2017.
 */

public class ReportDetail {
    public Observable<Report> getDetails(final int id, final String name) {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(Observer<? super Report> observer) {

                getReportDetails(id, name, observer);
            }
        };
    }

    private void getReportDetails(final int id, String name, final Observer<? super Report> observer) {
        ReportRepository repo = new ReportRemoteImplementation();
        ReportRepository repo2 = new ReportLocalImplementation();
        final Report finalReport = new Report();
        mergeDelayError(repo2.get(id, name), repo.get(id, name)).subscribe(new Observer<Report>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Report report) {
                if (finalReport.getName() == null)
                    finalReport.copy(report);
                else
                    finalReport.fillEmptyFields(report);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (finalReport.getName() == null)
                    observer.onError(e);
                else if (finalReport.getName() != null)
                    onComplete();

            }

            @Override
            public void onComplete() {
               findLocalObservations(finalReport,observer);
            }
        });
    }

    private void findLocalObservations(final Report report, final Observer<? super Report> observer)
    {
        new ObservationLocalImplementation().getReportObservations(report).subscribe(new Consumer<List<Observation>>() {
            @Override
            public void accept(@NonNull List<Observation> observations) throws Exception
            {
                addObservationsToReport(report,observations);
                new ReportLocalImplementation().modifiy(report).subscribe(new Consumer<Report>() {
                    @Override
                    public void accept(@NonNull Report report) throws Exception
                    {
                        observer.onNext(report);
                        observer.onComplete();
                    }
                });
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onNext(report);
                observer.onComplete();
            }
        });
    }

    private void addObservationsToReport(Report report, List<Observation> observations)
    {
        for (Observation o : observations)
        {
            Log.d("MOTA--->","Report Ob:"+o.getLocalId()+" I:"+o.getImages().size());
            if(!report.getObservations().contains(o))
                report.addObservation(o);
        }
    }

}
