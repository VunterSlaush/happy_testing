package mota.dev.happytesting.useCases;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.implementations.ObservationLocalImplementation;
import mota.dev.happytesting.repositories.implementations.ObservationRemoteImplementation;

/**
 * Created by Slaush on 25/06/2017.
 */

public class CreateObservation
{
    private static CreateObservation instance;

    private CreateObservation()
    {

    }

    public static CreateObservation getInstance()
    {
        if(instance == null)
            instance = new CreateObservation();
        return instance;
    }

    public Observable<Observation> create(final Report report, final String text)
    {
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(final Observer<? super Observation> observer)
            {
                if(report.getId() != -1)
                {
                    createObservationOnRemote(report,text,observer);
                }else
                {
                    createObservationOnLocal(report, text, observer);
                }
            }
        };
    }

    private void createObservationOnRemote(Report report, String text, final Observer<? super Observation> observer)
    {
        ObservationRemoteImplementation.getInstance().create(text,report).subscribe(new Consumer<Observation>()
        {
            @Override
            public void accept(@NonNull Observation observation) throws Exception
            {
                observer.onNext(observation);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                Log.d("MOTAE--->","Error>"+throwable.getMessage());
                observer.onError(new Throwable("No se pudo crear la Observacion"));
            }
        });
    }

    private void createObservationOnLocal(Report report, String text, final Observer<? super Observation> observer)
    {
        new ObservationLocalImplementation().create(text,report).subscribe(new Consumer<Observation>() {
            @Override
            public void accept(@NonNull Observation observation) throws Exception
            {
                Observation o = new Observation();
                o.copy(observation);
                observer.onNext(o);
                observer.onComplete();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                observer.onError(new Throwable("No se pudo crear la Observacion"));
            }
        });
    }
}
