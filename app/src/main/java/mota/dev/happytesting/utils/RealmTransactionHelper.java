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
        Log.d("MOTA--->","Inicio Transaccion");
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

    public interface OnTransaction
    {
        void action(Realm realm);
        void error(Exception e);
    }
}
