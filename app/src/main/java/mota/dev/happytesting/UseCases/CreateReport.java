package mota.dev.happytesting.useCases;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.managers.UserManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.repositories.implementations.ReportLocalImplementation;

/**
 * Created by Slaush on 24/06/2017.
 */

public class CreateReport {

    public Observable<Boolean> createReport(final App app, final String reportName)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                Report report = new Report();
                report.setAppName(app.getName());
                report.setName(reportName);
                report.setUsername(UserManager.getInstance().getUsername());
                report.setCreado(actualDateToString());
                report.setOwner_id(UserManager.getInstance().getUserId());
                app.addReport(report);
                saveOnLocal(report, observer);
            }
        };
    }

    private void saveOnLocal(Report report, final Observer<? super Boolean> observer)
    {
        ReportRepository repo = ReportLocalImplementation.getInstance();
        repo.create(report).subscribe(new Consumer<Report>() {
            @Override
            public void accept(@NonNull Report r) throws Exception
            {
                observer.onNext(true);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onNext(false);
                observer.onComplete();
            }
        });
    }

    private String actualDateToString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
}
