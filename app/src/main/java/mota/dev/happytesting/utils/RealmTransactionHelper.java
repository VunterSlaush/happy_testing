package mota.dev.happytesting.utils;

import io.realm.Realm;

/**
 * Created by Slaush on 29/06/2017.
 */

public class RealmTransactionHelper {




    public static void executeTransaction(OnTransaction actions)
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try
        {
            actions.action(realm);
        }catch (Exception e)
        {
            realm.cancelTransaction();
            actions.error(e);
        }
        realm.commitTransaction();
        try
        {
            realm.waitForChange();
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
