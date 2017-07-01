package mota.dev.happytesting.utils;

import android.util.Log;

import io.realm.Realm;

/**
 * Created by Slaush on 29/06/2017.
 */

public class RealmTransactionHelper {




    public static void executeTransaction(OnTransaction actions)
    {
        Log.d("MOTA--->","Inicio Transaccion");
        Realm realm = Realm.getDefaultInstance();
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
            Log.d("MOTA--->","Error Refrescando");
        }
       realm.close();
        Log.d("MOTA--->","Finalizo Transaccion");
    }

    public interface OnTransaction
    {
        void action(Realm realm);
        void error(Exception e);
    }
}
