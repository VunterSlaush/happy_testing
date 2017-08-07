package mota.dev.happytesting.repositories;

import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import mota.dev.happytesting.utils.RealmTransactionHelper;

/**
 * Created by user on 07/07/2017.
 */

public abstract class RealmRepository<T extends RealmObject>
{
    private static final String TAG = RealmRepository.class.getSimpleName();
    private Class<T> clazz;
    public RealmRepository()
    {
        this.clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Observable<Boolean> saveAll(final List<T> items)
    {
        Log.d(TAG, "Guardando Items del Tipo:"+clazz.getSimpleName());

        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer)
            {
                RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnResultTransaction() {

                    @Override
                    public Object action(Realm realm)
                    {
                        Log.d(TAG,"Se Guardaran:"+items.size() + " Items");
                        realm.copyToRealmOrUpdate(items);
                        return true;
                    }

                    @Override
                    public void onFinalize(Object o) {
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

    public void deleteItemsIfNeeded(final List<T> items)
    {
        Log.d(TAG, "Borrando Items del Tipo:"+clazz.getSimpleName());
        RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
            @Override
            public void action(Realm realm)
            {
                // Asumimos que todos los Items poseen ID!
                if (realm.where(clazz).greaterThanOrEqualTo("id", 0).count() != items.size())
                {
                    RealmResults<T> result = realm.where(clazz).greaterThanOrEqualTo("id", 0).findAll();
                    for (int i = 0; i < result.size(); i++)
                    {
                        if (!items.contains(result.get(i)))
                            result.deleteFromRealm(i);
                    }
                }
            }

            @Override
            public void error(Exception e) {

            }
        });


    }
}
