package mota.dev.happytesting.repositories.implementations;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmResults;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ObservationRepository;

/**
 * Created by Slaush on 25/06/2017.
 */

public class ObservationLocalImplementation implements ObservationRepository {
    @Override
    public Observable<Observation> create(final String text, final Report report) {
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(Observer<? super Observation> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Observation observation = realm.createObject(Observation.class);
                observation.setText(text);
                observation.setReportName(report.getName());
                realm.commitTransaction();
                observer.onNext(observation);
                observer.onComplete();
            }
        };
    }

    @Override
    public Observable<List<Observation>> getReportObservations(final Report report) {
        return new Observable<List<Observation>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Observation>> observer) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Observation> results = realm.where(Observation.class)
                                                         .equalTo("reportName",report.getName())
                                                         .findAll();
                List<Observation> list =  realm.copyFromRealm(results);
                observer.onNext(list);
                observer.onComplete();
            }
        };
    }

    @Override
    public Observable<Observation> get(final int id, final String text, final String reportName)
    {
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(Observer<? super Observation> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                try
                {

                    //realm.beginTransaction();
                    RealmResults<Observation> result = realm.where(Observation.class)
                            .equalTo("text",text)
                            .equalTo("reportName",reportName)
                            .findAll();
                    List<Observation> list =  realm.copyFromRealm(result);
                    Observation o = list.get(0);
                    //realm.commitTransaction();
                    observer.onNext(o);
                    observer.onComplete();

                }catch (Exception e)
                {
                    observer.onError(new Throwable("La observacion no existe:"+e.getMessage()));
                }

            }
        };
    }

    @Override
    public Observable<Observation> modify(final Observation o)
    {
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(Observer<? super Observation> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(o);
                realm.commitTransaction();
                observer.onNext(o);
                observer.onComplete();
            }
        };
    }

    @Override
    public Observable<Boolean> delete(final Observation o) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                Realm realm = Realm.getDefaultInstance();
                try
                {

                    realm.beginTransaction();
                    RealmResults<Observation> result = realm.where(Observation.class)
                            .equalTo("text",o.getText())
                            .equalTo("reportName",o.getReportName())
                            .equalTo("id",o.getId())
                            .findAll();
                    result.deleteAllFromRealm();
                    realm.commitTransaction();
                    observer.onNext(true);

                }catch (Exception e)
                {
                    realm.cancelTransaction();
                    observer.onNext(false);
                }
                observer.onComplete();
            }
        };
    }
}
