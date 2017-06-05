package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.repositories.AppRepository;

/**
 * Created by Slaush on 29/05/2017.
 */

public class AppLocalImplementation implements AppRepository
{

    public AppLocalImplementation()
    {

    }

    @Override
    public Observable<App> create(final String name)
    {
        return new Observable<App>()
        {
            @Override
            protected void subscribeActual(Observer<? super App> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                App app = realm.createObject(App.class, name);
                realm.commitTransaction();
                observer.onNext(app);
                observer.onComplete();
            }
        };

    }

    @Override
    public Observable<List<App>> getAll()
    {
        return new Observable<List<App>>()
        {
            @Override
            protected void subscribeActual(Observer<? super List<App>> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<App> results = realm.where(App.class).findAll();
                List<App> list =  realm.copyFromRealm(results);
                observer.onNext(list);
                observer.onComplete();
            }
        };

    }

    @Override
    public Observable<App> modifiy(final App app)
    {
        return new Observable<App>() {
            @Override
            protected void subscribeActual(Observer<? super App> observer)
            {
                Log.d("BINDEANDO","CREANDO APP!!");
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(app);
                realm.commitTransaction();
                observer.onNext(app);
                observer.onComplete();
            }
        };
    }

    @Override
    public Observable<Boolean> delete(final App app)
    {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer)
            {
                try
                {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<App> result = realm.where(App.class).equalTo("name",app.getName()).findAll();
                    result.deleteAllFromRealm();
                    observer.onNext(true);
                    observer.onComplete();
                }catch (Exception e)
                {
                    observer.onNext(false); // No se Borro la aplicacion
                }

            }
        };
    }

    @Override
    public void updateApps(List<App> apps)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for(App app: apps)
        {
            try
            {
                realm.copyToRealmOrUpdate(app);
            }
            catch(Exception e)
            {

            }

        }
        realm.commitTransaction();
    }
}
