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
    public Observable<Observation> get(int id) {
        return null;
    }

    @Override
    public Observable<Observation> modify(Observation o) {
        return null;
    }

    @Override
    public Observable<Boolean> delete(Observation o) {
        return null;
    }
}
