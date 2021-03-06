package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.managers.RequestManager;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.parsers.ObservationParser;
import mota.dev.happytesting.repositories.ObservationRepository;
import mota.dev.happytesting.utils.RxHelper;

/**
 * Created by Slaush on 05/07/2017.
 */

public class ObservationRemoteImplementation implements ObservationRepository
{
    private static ObservationRemoteImplementation instance;
    private ObservationRemoteImplementation(){}

    public static ObservationRemoteImplementation getInstance()
    {
        if(instance == null)
            instance = new ObservationRemoteImplementation();
        return instance;
    }

    @Override
    public Observable<Observation> create(final String text, final Report report)
    {
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(final Observer<? super Observation> observer)
            {
                final JSONObject obJson = ObservationParser.getInstance().generateJSONObservationToSend(text, report);
                RequestManager.getInstance()
                        .sendObservation(obJson)
                        .subscribe(new Consumer<JSONObject>()
                {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception {
                        Observation o = ObservationParser.getInstance()
                                .generateObservationFromJson(jsonObject.optJSONObject("res"));
                        observer.onNext(o);
                        observer.onComplete();
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

    @Override
    public Observable<List<Observation>> getReportObservations(Report report) {
        return null;
    }

    @Override
    public Observable<Observation> get(int id, String localId) {
        return null;
    }

    @Override
    public Observable<Observation> modify(Observation o) {
        return null;
    }

    @Override
    public Observable<Boolean> delete(final Observation o)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer) {

                try
                {
                    JSONObject json = new JSONObject();
                    json.put("id", o.getId());
                    json.put("reporte", o.getReportName());
                    RequestManager.getInstance()
                                  .deleteObservation(json)
                                  .subscribe(RxHelper.getSuccessConsumer(observer),
                                             RxHelper.getErrorThrowable(observer,false));
                }catch (Exception e)
                {
                    observer.onError(new Throwable("no se pudo borrar la Observacion"));
                }
            }
        };
    }
}
