package mota.dev.happytesting;

import android.content.Context;
import android.content.Intent;

import io.realm.Realm;
import mota.dev.happytesting.Views.activities.LoginActivity;
import mota.dev.happytesting.managers.RouterManager;
import mota.dev.happytesting.managers.UserManager;
import mota.dev.happytesting.utils.RealmTransactionHelper;

/**
 * Created by Slaush on 15/05/2017.
 */

public class MyApplication extends android.app.Application
{
    private static MyApplication instance;
    private Realm realmInstance;
    private Realm uiInstance;
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        Realm.init(this);
        RouterManager.getInstance().findServerUrl();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public void logout(Context context)
    {
        deleteAllDatabase();
        UserManager.getInstance().logout();
        goToLoginActivity(context);
    }

    public void deleteAllDatabase()
    {
        RealmTransactionHelper.executeTransaction(new RealmTransactionHelper.OnTransaction() {
            @Override
            public void action(Realm realm) {
                realm.deleteAll();
            }

            @Override
            public void error(Exception e) {

            }
        });
    }

    private void goToLoginActivity(Context context)
    {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

    public Realm getRealmInstance()
    {
        if(realmInstance == null)
            realmInstance = Realm.getDefaultInstance();

        return realmInstance;
    }

    public Realm getRealmUiInstance()
    {
        if(uiInstance == null)
            uiInstance = Realm.getDefaultInstance();
        return uiInstance;
    }
}
