package mota.dev.happytesting.useCases;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.implementations.ImageLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ReportLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ReportRemoteImplementation;
import mota.dev.happytesting.utils.RxHelper;

/**
 * Created by user on 07/07/2017.
 */

public class GetAllDataToStore
{
    private static GetAllDataToStore instance;
    private static String TAG = GetAllDataToStore.class.getSimpleName();

    private GetAllDataToStore(){}

    public static GetAllDataToStore getInstance()
    {
        if(instance == null)
            instance = new GetAllDataToStore();
        return instance;
    }

    public Observable<Boolean> getData()
    {
      return new Observable<Boolean>()
      {
          @Override
          protected void subscribeActual(final Observer<? super Boolean> observer)
          {
               new GetReports().fetchReports().subscribe(new Consumer<List<Report>>()
               {
                   @Override
                   public void accept(@NonNull List<Report> reports) throws Exception
                   {
                       Log.d(TAG, "Reportes Conseguidos");
                       getReportsDetails(reports, observer);
                   }
               }, RxHelper.getErrorThrowable(observer,false));
          }
      };
    }

    private void getReportsDetails(final List<Report> reports, final Observer<? super Boolean> observer)
    {
        final List<Report> reports1 = new ArrayList<>();
        List<Observable<Report>> observables = new ArrayList<>();
        for (Report r : reports)
            observables.add(new ReportRemoteImplementation().get(r.getId(),r.getName()));

        Observable.concatDelayError(observables).subscribe(new Observer<Report>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Report report) {
                reports1.add(report);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                saveReports(reports1,observer);
            }
        });
    }

    private void saveReports(final List<Report> reports, final Observer<? super Boolean> observer)
    {
        ReportLocalImplementation.getInstance().deleteItemsIfNeeded(reports);
        ReportLocalImplementation.getInstance().saveAll(reports).subscribe(new Consumer<Boolean>()
        {
            @Override
            public void accept(@NonNull Boolean result) throws Exception
            {
                Log.d(TAG, "Reportes Guardados");
                if(result)
                    generateObservations(reports, observer);
                else
                    RxHelper.nextAndComplete(observer,false);
            }
        }, RxHelper.getErrorThrowable(observer,false));
    }

    private void generateObservations(List<Report> reports, Observer<? super Boolean> observer)
    {
        Log.d(TAG, "Observaciones Sacadas");
        List<Observation> obs = new ArrayList<>();
        for (Report r : reports)
            obs.addAll(r.getObservations());
        saveObservations(obs, observer);
    }

    private void saveObservations(final List<Observation> obs, final Observer<? super Boolean> observer)
    {
        new ObservationLocalImplementation().deleteItemsIfNeeded(obs);
        new ObservationLocalImplementation().saveAll(obs).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean result) throws Exception
            {
                if(result)
                    generateImages(obs, observer);
                else
                    RxHelper.nextAndComplete(observer,false);
            }
        },  RxHelper.getErrorThrowable(observer,false));
    }

    private void generateImages(List<Observation> obs, Observer<? super Boolean> observer)
    {
        List<Image> images  = new ArrayList<>();
        for (Observation r : obs)
            images.addAll(r.getImages());
        saveImages(images, observer);
    }

    private void saveImages(List<Image> images, final Observer<? super Boolean> observer)
    {
        ImageLocalImplementation.getInstance().deleteItemsIfNeeded(images);
        ImageLocalImplementation.getInstance().saveAll(images).subscribe(new Consumer<Boolean>()
        {
            @Override
            public void accept(@NonNull Boolean result) throws Exception
            {
                RxHelper.nextAndComplete(observer,result);
            }
        }, RxHelper.getErrorThrowable(observer, false));
    }

}
