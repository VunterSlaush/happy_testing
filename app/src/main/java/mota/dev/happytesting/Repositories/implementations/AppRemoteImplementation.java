package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.Consts;
import mota.dev.happytesting.managers.ErrorManager;
import mota.dev.happytesting.managers.RequestManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.parsers.AppParser;
import mota.dev.happytesting.repositories.AppRepository;

/**
 * Created by Slaush on 29/05/2017.
 */

public class AppRemoteImplementation implements AppRepository {

    @Override
    public Observable<App> create(final String name, final List<User> users)
    {
        return new Observable<App>() {
            @Override
            protected void subscribeActual(final Observer<? super App> observer)
            {
                Observable<JSONObject> ob = RequestManager.getInstance().createApp(name,convertToUsersIdsArray(users));
                ob.subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception
                    {
                        try
                        {
                            App app = AppParser.getInstance().jsonToApp(jsonObject);
                            observer.onNext(app);
                            observer.onComplete();
                        }
                        catch (Exception e)
                        {
                            observer.onError(ErrorManager.getInstance().getError(jsonObject));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        observer.onError(throwable);
                    }
                });
            }
        };

    }

    private int[] convertToUsersIdsArray(List<User> users)
    {
        int [] ids = new int [users.size()];
        for (int i = 0; i<users.size(); i++)
        {
            ids[i] = users.get(i).getId();
        }
        return ids;
    }

    @Override
    public Observable<List<App>> getAll() {
        return new Observable<List<App>>() {
            @Override
            protected void subscribeActual(final Observer<? super List<App>> observer)
            {
                Observable<JSONObject> ob = RequestManager.getInstance().getApps();
                ob.subscribe(new Consumer<JSONObject>() {
                                 @Override
                                 public void accept(@NonNull JSONObject jsonObject) throws Exception 
                                 {  
                                     try 
                                     {
                                         List<App> apps = AppParser.getInstance().getAppsOfJson(jsonObject);
                                         observer.onNext(apps);
                                         observer.onComplete();
                                     }catch (Exception e)
                                     {
                                         observer.onError(ErrorManager.getInstance().getError(jsonObject));
                                     }

                                 }
                             },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                observer.onError(throwable);
                            }
                        });
            }
        };
    }

    @Override
    public Observable<App> get(final int id) {
        Log.d("MOTA--->","ENTRANDO EN GET");
        return new Observable<App>() {
            @Override
            protected void subscribeActual(final Observer<? super App> observer)
            {
                Log.d("MOTA--->","LLAMANDO GET");
                Observable<JSONObject> ob = RequestManager.getInstance().getApp(id);
                ob.subscribe(new Consumer<JSONObject>() {
                                 @Override
                                 public void accept(@NonNull JSONObject jsonObject) throws Exception
                                 {
                                     Log.d("MOTA--->","ENTRANDO en ACCEPT del GET!");
                                     try
                                     {
                                         App app = AppParser.getInstance().jsonToApp(jsonObject, "app");
                                         observer.onNext(app);
                                         observer.onComplete();
                                     }catch (Exception e)
                                     {
                                         Log.d("MOTA--->","ENVIANDO EXCEPTION, QUIZAS DE PARSER!"+e.getMessage());
                                         observer.onError(ErrorManager.getInstance().getError(jsonObject));
                                     }

                                 }
                             },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                Log.d("MOTA--->","GET Error:"+throwable.getMessage());
                                observer.onError(throwable);
                            }
                        });
            }
        };
    }


    @Override
    public Observable<App> modifiy(App app) {
        return null;
    }

    @Override
    public Observable<Boolean> delete(App app) {
        return null;
    }

    @Override
    public void updateApps(List<App> apps) {
        //
    }



}
