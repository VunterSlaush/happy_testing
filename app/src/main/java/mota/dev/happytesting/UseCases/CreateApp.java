package mota.dev.happytesting.useCases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.MainActivity;
import mota.dev.happytesting.managers.ErrorManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppLocalImplementation;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;

/**
 * Created by Slaush on 29/05/2017.
 */

public class CreateApp
{
    private AppRepository remoteRepository, localRepository;
    private String app_name;
    private Context context;
    private List<User> selected_users;

    public CreateApp(Context context)
    {
        this.context = context;
        remoteRepository = AppRemoteImplementation.getInstance();
        localRepository = AppLocalImplementation.getInstance();
    }

    public void createApp(String name, List<User> selected)
    {
        this.app_name = name;
        this.selected_users = selected;
        localRepository.create(name,selected)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<App>() {
                            @Override
                            public void accept(@NonNull App app) throws Exception {
                                createAppOnRemote();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Toast.makeText(context,"La aplicacion no pudo ser creada", Toast.LENGTH_SHORT).show();
                            }
                        });
    }



    private void finishCreate()
    {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
        ((Activity) context).finish();
    }


    private void createAppOnRemote()
    {
        remoteRepository.create(app_name,this.selected_users)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<App>() {
                            @Override
                            public void accept(@NonNull App app) throws Exception
                            {
                                Toast.makeText(context,"Aplicacion enviada al servidor",Toast.LENGTH_SHORT).show();
                                localRepository.modifiy(app).subscribe(new Consumer<App>() {
                                    @Override
                                    public void accept(@NonNull App app) throws Exception {
                                        finishCreate();
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                        Toast.makeText(context,"Error Inesperado",Toast.LENGTH_SHORT).show();
                                        finishCreate();
                                    }
                                });
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Toast.makeText(context,"La Aplicacion no pudo ser enviada al servidor",Toast.LENGTH_SHORT).show();
                            }
                        });
    }

}
