package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmResults;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ObservationRepository;
import mota.dev.happytesting.repositories.RealmRepository;
import mota.dev.happytesting.utils.Functions;
import mota.dev.happytesting.utils.RealmTransactionHelper;

/**
 * Created by Slaush on 25/06/2017.
 */

public class ObservationLocalImplementation extends RealmRepository<Observation>
                                            implements ObservationRepository
{

    private static final String TAG = ObservationLocalImplementation.class.getSimpleName();

    @Override
    public Observable<Observation> create(final String text, final Report report) {
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(final Observer<? super Observation> observer) {

                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm) {
                        Observation observation = realm.createObject(Observation.class, Functions.generateRandomId());
                        observation.setText(text);
                        observation.setReportName(report.getName());
                        observer.onNext(observation);
                        observer.onComplete();
                    }

                    @Override
                    public void error(Exception e) {
                        Log.d("MOTA--->","EXCEPT:"+e.getMessage());
                        observer.onError(new Throwable("no se pudo crear la observacion"));
                    }
                });
            }
        };
    }

    @Override
    public Observable<List<Observation>> getReportObservations(final Report report) {
        return new Observable<List<Observation>>() {
            @Override
            protected void subscribeActual(final Observer<? super List<Observation>> observer) {
                // TODO FIX!
                Log.d(TAG,"Get Report Observations");
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Observation> results = realm.where(Observation.class)
                        .equalTo("reportName", report.getName())
                        .findAll();

                List<Observation> list = realm.copyFromRealm(results);
                observer.onNext(list);
                observer.onComplete();
                realm.close();
                Log.d(TAG,"Se Cerro la instancia de Realm !");

            }
        };
    }

    @Override
    public Observable<Observation> get(final int id, final String localId) {
        Log.e(TAG,"Get OB ID:"+id + " LocalID:"+localId);
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(Observer<? super Observation> observer) {
                Realm realm = Realm.getDefaultInstance();
                try {
                    Observation o = realm.where(Observation.class)
                            .equalTo("localId", localId)
                            .or()
                            .equalTo("id",id)
                            .findFirst();

                    observer.onNext(o);
                    observer.onComplete();

                } catch (Exception e) {
                    Log.e("MOTA--->","Except:"+e.getMessage());
                    observer.onError(new Throwable("La observacion no existe:" + e.getMessage()));
                }
                realm.close();
            }
        };
    }

    @Override
    public Observable<Observation> modify(final Observation o) {
        return new Observable<Observation>() {
            @Override
            protected void subscribeActual(final Observer<? super Observation> observer) {

            RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                @Override
                public void action(Realm realm) {
                    realm.copyToRealmOrUpdate(o);
                    observer.onNext(o);
                    observer.onComplete();
                }

                @Override
                public void error(Exception e) {
                    Log.d("MOTA--->","Except>"+e.getMessage());
                    observer.onError(new Throwable("Ocurrio un error modificando"));
                }
            });

            }
        };
    }

    @Override
    public Observable<Boolean> delete(final Observation o) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer) {
                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm) {
                        RealmResults<Observation> result = realm.where(Observation.class)
                                .equalTo("localId", o.getLocalId())
                                .or()
                                .equalTo("id",o.getId())
                                .findAll();
                        result.deleteAllFromRealm();
                        observer.onNext(true);
                        observer.onComplete();
                    }

                    @Override
                    public void error(Exception e) {
                        observer.onNext(false);
                        observer.onComplete();
                    }
                });
            }
        };
    }

}
