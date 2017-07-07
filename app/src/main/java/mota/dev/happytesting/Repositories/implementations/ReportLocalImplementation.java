package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmResults;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.managers.UserManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.RealmRepository;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.useCases.SendReport;
import mota.dev.happytesting.utils.RealmTransactionHelper;

/**
 * Created by Slaush on 24/06/2017.
 */

public class ReportLocalImplementation extends RealmRepository<Report> implements ReportRepository {

    private static String TAG = ReportLocalImplementation.class.getSimpleName();
    private static ReportLocalImplementation instance;
    private ReportLocalImplementation(){}

    public static ReportLocalImplementation getInstance()
    {
        if(instance == null)
            instance = new ReportLocalImplementation();
        return instance;
    }

    @Override
    public Observable<List<Report>> getAll() {
        return new Observable<List<Report>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Report>> observer) {
                Realm realm = Realm.getDefaultInstance();
                Log.d(TAG,"get All Reports USER ID"+UserManager.getInstance().getUserId());

                RealmResults<Report> results = realm.where(Report.class)
                        .equalTo("owner_id", UserManager.getInstance().getUserId()).findAll();
                List<Report> list = realm.copyFromRealm(results);

                Log.d(TAG,"get All Reports"+list.size());

                observer.onNext(list);
                observer.onComplete();
                realm.close();

            }
        };

    }

    @Override
    public Observable<List<Report>> getAppReports(final App app) {
        return new Observable<List<Report>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Report>> observer) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Report> results = realm.where(Report.class).equalTo("appName", app.getName()).findAll();
                List<Report> list = realm.copyFromRealm(results);
                observer.onNext(list);
                observer.onComplete();
                realm.close();
            }
        };

    }

    @Override
    public Observable<Report> get(final int id, final String name) {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(final Observer<? super Report> observer) {

                Realm realm = Realm.getDefaultInstance();
                Report report = realm.where(Report.class)
                                                    .equalTo("id", id)
                                                    .equalTo("name", name).findFirst();

                if (report == null) {
                    observer.onError(new Throwable("Reporte no encontrado"));
                    return;
                }
                Report reportCopy = new Report();
                reportCopy.copy(report);
                observer.onNext(reportCopy);
                observer.onComplete();
                realm.close();

            }
        };
    }

    @Override
    public Observable<Report> create(final Report report) {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(final Observer<? super Report> observer) {
                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm) {
                        Report report1 = realm.createObject(Report.class, report.getName() + "-" + report.getAppName());
                        report1.copy(report);
                        observer.onNext(report1);
                        observer.onComplete();
                    }

                    @Override
                    public void error(Exception e) {
                        Log.e(TAG,"Ex:"+e.getMessage());
                        observer.onError(new Throwable("Ya Existe un reporte con este nombre"));
                    }
                });

            }
        };
    }

    @Override
    public Observable<Report> modifiy(final Report report) {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(final Observer<? super Report> observer) {
                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm) {
                        realm.copyToRealmOrUpdate(report);
                        observer.onNext(report);
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
    public Observable<Boolean> delete(final Report report) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer) {

                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
                    @Override
                    public void action(Realm realm) {
                        RealmResults<Report> result = realm.where(Report.class)
                                .equalTo("name", report.getName())
                                .equalTo("appName", report.getAppName())
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
