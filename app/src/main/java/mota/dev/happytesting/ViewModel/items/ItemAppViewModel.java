package mota.dev.happytesting.ViewModel.items;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import java.util.ArrayList;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Views.activities.DetailAppActivity;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;
import mota.dev.happytesting.useCases.SendApp;

/**
 * Created by Slaush on 30/05/2017.
 */

public class ItemAppViewModel extends Observable {

    public ObservableInt appId;
    public ObservableField<String> appName;
    public ObservableField<String> sendText;
    public ObservableBoolean enableButton;
    private Context context;
    private App app;

    public ItemAppViewModel(App app, Context context)
    {
        this.context = context;
        appId = new ObservableInt(app.getId());
        appName = new ObservableField<>(app.getName());
        sendText = new ObservableField<>("Enviar");
        enableButton = new ObservableBoolean(true);
    }

    public void setApp(App app)
    {
        appId.set(app.getId());
        appName.set(app.getName());
        this.app = app;
    }

    public void enviar(View view)
    {
        sendText.set("Enviando");
        enableButton.set(false);
        SendApp.getInstance().send(app)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<App>() {
                @Override
                public void accept(@NonNull App app) throws Exception {
                    appId.set(app.getId());
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    sendText.set("Enviar");
                    enableButton.set(true);
                }
            });
    }

    public void abrir(View view)
    {
        Intent i = new Intent(context, DetailAppActivity.class);
        i.putExtra("app_id",appId.get());
        i.putExtra("app_name",appName.get());
        context.startActivity(i);
    }
}
