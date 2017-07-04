package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmResults;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.utils.RealmTransactionHelper;

/**
 * Created by Slaush on 29/05/2017.
 */

public class AppLocalImplementation implements AppRepository {


    private static AppLocalImplementation instance;

    private AppLocalImplementation() {
    }

    public static AppLocalImplementation getInstance() {
        if (instance == null)
            instance = new AppLocalImplementation();
        return instance;
    }

    @Override
    public Observable<App> create(final String name, final List<User> users) {


        return new Observable<App>() {
            @Override
            protected void subscribeActual(final Observer<? super App> observer) {

                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnResultTransaction<App>()
                {
                    @Override
                    public App action(Realm realm) {
                        App app = realm.createObject(App.class, name);
                        app.setUsers(users);
                        final App localApp = new App();
                        localApp.copy(app);
                        return localApp;
                    }

                    @Override
                    public void onFinalize(App app) {
                        observer.onNext(app);
                        observer.onComplete();
                    }

                    @Override
                    public void error(Exception e) {
                        Log.d("MOTA--->","ERRRO>"+e.getMessage());
                        observer.onError(new Throwable("Aplicacion Existente!"));
                    }
                });


            }
        };

    }

    @Override
    public Observable<List<App>> getAll() {
        return new Observable<List<App>>() {
            @Override
            protected void subscribeActual(Observer<? super List<App>> observer) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<App> results = realm.where(App.class).findAll();
                List<App> list = realm.copyFromRealm(results);
                observer.onNext(list);
                observer.onComplete();
                realm.close();
            }
        };

    }

    @Override
    public Observable<App> get(final int id, final String appName) {
        return new Observable<App>() {
            @Override
            protected void subscribeActual(Observer<? super App> observer) {
                Realm realm = Realm.getDefaultInstance();
                App app = realm.where(App.class).equalTo("id", id).or().equalTo("name", appName).findFirst();
                if (app != null) {
                    observer.onNext(app);
                    observer.onComplete();
                } else {
                    observer.onError(new Throwable("Aplicacion no encontrada"));
                }
                realm.close();
            }
        };
    }

    @Override
    public Observable<App> modifiy(final App app) {
        return new Observable<App>() {
            @Override
            protected void subscribeActual(final Observer<? super App> observer) {

                Log.d("MOTA--->", "Modificando App:" + app.getName());
                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm) {
                        realm.copyToRealmOrUpdate(app);
                        observer.onNext(app);
                        observer.onComplete();
                    }

                    @Override
                    public void error(Exception e) {
                        observer.onError(new Throwable("Error inesperado"));
                    }
                });


            }
        };
    }

    @Override
    public Observable<Boolean> delete(final App app) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer) {


                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm) {
                        RealmResults<App> result = realm.where(App.class)
                                .equalTo("name", app.getName())
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

    @Override
    public void updateApps(final List<App> apps) {
        RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
            @Override
            public void action(Realm realm)
            {
                deleteAppsIfNeeded(realm, apps);
                for (App app : apps) {
                    try {
                        App appSaved = realm.where(App.class).equalTo("name", app.getName()).findFirst();
                        if (appSaved != null)
                            appSaved.setId(app.getId());
                        else
                            realm.copyToRealmOrUpdate(app);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
    }

    private void deleteAppsIfNeeded(Realm realm, List<App> apps) {
        if (realm.where(App.class).greaterThanOrEqualTo("id", 0).count() != apps.size()) {
            RealmResults<App> result = realm.where(App.class).greaterThanOrEqualTo("id", 0).findAll();
            for (int i = 0; i < result.size(); i++) {
                if (!apps.contains(result.get(i)))
                    result.deleteFromRealm(i);
            }
        }
    }
}
