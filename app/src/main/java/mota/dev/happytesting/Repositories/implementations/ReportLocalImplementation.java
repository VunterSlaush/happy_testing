package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import mota.dev.happytesting.managers.ErrorManager;
import mota.dev.happytesting.managers.UserManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ReportRepository;

/**
 * Created by Slaush on 24/06/2017.
 */

public class ReportLocalImplementation implements ReportRepository
{
    @Override
    public Observable<List<Report>> getAll() {
        return new Observable<List<Report>>()
        {
            @Override
            protected void subscribeActual(Observer<? super List<Report>> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Report> results = realm.where(Report.class)
                                                .equalTo("owner_id", UserManager.getInstance().getUserId()).findAll();
                List<Report> list =  realm.copyFromRealm(results);
                for (Report r : list)
                    Log.d("MOTA--->","PASANDO"+r.getName());
                observer.onNext(list);
                observer.onComplete();
            }
        };

    }

    @Override
    public Observable<List<Report>> getAppReports(final App app) {
        return new Observable<List<Report>>()
        {
            @Override
            protected void subscribeActual(Observer<? super List<Report>> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Report> results = realm.where(Report.class).equalTo("appName",app.getName()).findAll();
                List<Report> list =  realm.copyFromRealm(results);
                observer.onNext(list);
                observer.onComplete();
            }
        };

    }

    @Override
    public Observable<Report> get(final int id, final String name) {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(Observer<? super Report> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Report> results = realm.where(Report.class).equalTo("id", id).or().equalTo("name",name).findAll();
                List<Report> list =  realm.copyFromRealm(results);

                if(list.size() <= 0)
                {
                    observer.onError(new Throwable("Aplicacion no encontrada"));
                    return;
                }

                Report report = list.get(0);
                observer.onNext(report);
                observer.onComplete();
            }
        };
    }

    @Override
    public Observable<Report> create(final Report report) {
        return new Observable<Report>()
        {
            @Override
            protected void subscribeActual(Observer<? super Report> observer)
            {   Realm realm = Realm.getDefaultInstance();
                try
                {

                    realm.beginTransaction();
                    Report report1 = realm.createObject(Report.class,report.getName()+"-"+report.getAppName());
                    report1.copy(report);
                    realm.commitTransaction();
                    observer.onNext(report1);
                    observer.onComplete();
                }catch (RealmPrimaryKeyConstraintException e)
                {
                    realm.cancelTransaction();
                    observer.onError(new Throwable("Ya Existe un Reporte con este Nombre"));

                }

            }
        };
    }

    @Override
    public Observable<Report> modifiy(final Report report) {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(Observer<? super Report> observer) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(report);
                realm.commitTransaction();
                observer.onNext(report);
                observer.onComplete();
            }
        };
    }

    @Override
    public Observable<Boolean> delete(final Report report) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer)
            {
                Realm realm = Realm.getDefaultInstance();
                try
                {

                    realm.beginTransaction();
                    RealmResults<Report> result = realm.where(Report.class)
                                                       .equalTo("name",report.getName())
                                                       .equalTo("appName",report.getAppName())
                                                       .findAll();
                    Log.d("MOTA--->","FILAS A BORRAR:"+result.size());
                    result.deleteAllFromRealm();
                    realm.commitTransaction();
                    observer.onNext(true);
                }catch (Exception e)
                {
                    realm.cancelTransaction();
                    observer.onNext(false); // No se Borro la aplicacion
                }
                observer.onComplete();
            }
        };
    }
}
