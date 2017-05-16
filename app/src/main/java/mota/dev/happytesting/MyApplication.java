package mota.dev.happytesting;

import android.content.Context;

import mota.dev.happytesting.managers.RouterManager;

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
        RouterManager.getInstance().findServerUrl();
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
