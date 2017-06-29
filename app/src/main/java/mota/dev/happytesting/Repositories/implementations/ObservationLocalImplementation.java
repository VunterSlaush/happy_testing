package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmResults;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ObservationRepository;
import mota.dev.happytesting.utils.Functions;

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
                Observation observation = realm.createObject(Observation.class, Functions.generateRandomId());
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
            protected void subscribeActual(final Observer<? super List<Observation>> observer) {

                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm)
                    {
                        RealmResults<Observation> results = realm.where(Observation.class)
                                .equalTo("reportName",report.getName())
                                .findAll();
                        List<Observation> list =  realm.copyFromRealm(results);
                        for (Observation o : list) {
                            findObservationImages(realm,o);
                            Log.d("MOTA--->","Observation:"+o.getLocalId()+" Images:"+o.getImages().size());
                        }
                        observer.onNext(list);
                        observer.onComplete();
                    }
                });

            }
        };
    }

    private void findObservationImages(Realm realm, Observation o)
    {
        RealmResults<Image> results = realm.where(Image.class)
                                           .equalTo("observationName",o.getLocalId())
                                           .findAll();
        o.setImages(realm.copyFromRealm(results));
    }

    @Override
    public Observable<Observation> get(final String id)
    {
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(Observer<? super Observation> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                try
                {
                    RealmResults<Observation> result = realm.where(Observation.class)
                            .equalTo("localId",id)
                            .findAll();
                    List<Observation> list =  realm.copyFromRealm(result);
                    Observation o = list.get(0);
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
            protected void subscribeActual(final Observer<? super Observation> observer)
            {

                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm)
                    {
                        realm.copyToRealmOrUpdate(o);
                        borrarImagenes(realm,o);
                        realm.copyToRealmOrUpdate(o.getImages());
                        observer.onNext(o);
                        observer.onComplete();
                    }
                });



            }
        };
    }

    private void borrarImagenes(Realm realm, Observation o)
    {
        try {
            RealmResults<Image> result = realm.where(Image.class)
                    .equalTo("observationName",o.getLocalId())
                    .findAll();
            result.deleteAllFromRealm();
        }catch (Exception e)
        {
            Log.d("MOTA--->","QUEJESTO??");
        }

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
                            .equalTo("localId",o.getLocalId())
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
