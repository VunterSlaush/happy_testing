package mota.dev.happytesting;

import android.content.Context;
import android.content.Intent;

import io.realm.Realm;
import mota.dev.happytesting.Views.activities.LoginActivity;
import mota.dev.happytesting.managers.RouterManager;
import mota.dev.happytesting.managers.UserManager;

/**
 * Created by Slaush on 15/05/2017.
 */

public class MyApplication extends android.app.Application
{
    private static MyApplication instance;
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
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    private void goToLoginActivity(Context context)
    {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }
}
