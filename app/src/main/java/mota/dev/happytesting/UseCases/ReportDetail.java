package mota.dev.happytesting.useCases;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;
import mota.dev.happytesting.repositories.implementations.ImageLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ReportLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ReportRemoteImplementation;
import mota.dev.happytesting.utils.RxHelper;

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

                if (id != -1)
                    getReportDetailsRemoteAndLocal(id, name, observer);
                else
                    getLocalReportDetails(name, observer);
            }
        };
    }

    private void getLocalReportDetails(String name, final Observer<? super Report> observer)
    {
        ReportRepository repo = ReportLocalImplementation.getInstance();
        repo.get(-1,name).subscribe(new Consumer<Report>() {
            @Override
            public void accept(@NonNull Report report) throws Exception {

                observer.onNext(report);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(new Throwable("no se pudo conseguir el reporte"));
            }
        });

    }

    private void getReportDetailsRemoteAndLocal(final int id, String name, final Observer<? super Report> observer) {
        ReportRepository repo = new ReportRemoteImplementation();
        ReportRepository repo2 = ReportLocalImplementation.getInstance();
        final Report finalReport = new Report();
        mergeDelayError(repo2.get(id, name),repo.get(id, name)).subscribe(new Observer<Report>()
        {

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
            public void onComplete()
            {
                saveObservationsToLocal(finalReport, observer);
            }
        });
    }

    private void saveObservationsToLocal(final Report finalReport, final Observer<? super Report> observer)
    {
        new ObservationLocalImplementation().saveAll(finalReport.getObservations()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                saveImagesToLocal(finalReport,observer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                RxHelper.nextAndComplete(observer, finalReport);
            }
        });
    }

    private void saveImagesToLocal(final Report finalReport, final Observer<? super Report> observer)
    {
        List<Image> images = new ArrayList<>();
        for (Observation o : finalReport.getObservations())
            images.addAll(o.getImages());

        ImageLocalImplementation.getInstance().saveAll(images).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception
            {
                RxHelper.nextAndComplete(observer, finalReport);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                RxHelper.nextAndComplete(observer, finalReport);
            }
        });
    }

    public Observable<List<Observation>> findLocalObservations(final Report report)
    {
        return new ObservationLocalImplementation().getReportObservations(report);
    }

}
