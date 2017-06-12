package mota.dev.happytesting.useCases;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.repositories.implementations.ReportRemoteImplementation;

/**
 * Created by Slaush on 11/06/2017.
 */

public class GetReports
{

    public Observable<List<Report>> fetchReports()
    {
        final ReportRepository repo = new ReportRemoteImplementation();
        return new Observable<List<Report>>()
        {
            @Override
            protected void subscribeActual(final Observer<? super List<Report>> observer)
            {
                repo.getAll().subscribe(new Consumer<List<Report>>()
                {
                    @Override
                    public void accept(@NonNull List<Report> reports) throws Exception
                    {
                        observer.onNext(reports);
                        observer.onComplete();
                        //TODO acciones que se tengan que hacer despues o antes de conseguir los reportes?
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        observer.onError(throwable);
                    }
                });
            }
        };
    }
}
