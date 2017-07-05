package mota.dev.happytesting.utils;

import android.os.Handler;
import android.util.Log;

import io.realm.Realm;

/**
 * Created by Slaush on 29/06/2017.
 */

public class RealmTransactionHelper {

    public static void executeTransaction(OnTransaction actions)
    {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try
        {
            actions.action(realm);
            realm.commitTransaction();
        }catch (Exception e)
        {
            realm.cancelTransaction();
            actions.error(e);
        }
        try
        {
            realm.refresh();
        }catch (Exception e)
        {

        }
        realm.close();
    }

    public static void executeTransaction(OnResultTransaction actions)
    {
        Object result = null;
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try
        {
            result = actions.action(realm);
            realm.commitTransaction();
        }
        catch (Exception e)
        {
            realm.cancelTransaction();
            actions.error(e);
        }
        try
        {
            realm.refresh();
        }catch (Exception e)
        {

        }
        actions.onFinalize(result);
        realm.close();
    }

    public interface OnTransaction
    {
        void action(Realm realm);
        void error(Exception e);
    }

    public interface OnResultTransaction<T>
    {

        T action(Realm realm);
        void onFinalize(T t);
        void error(Exception e);
    }
}
