package mota.dev.happytesting.repositories.implementations;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
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
import mota.dev.happytesting.utils.Functions;

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
                Observable<JSONObject> ob = RequestManager.getInstance().createApp(name, Functions.convertToUsersIdsArray(users));
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

        return new Observable<App>() {
            @Override
            protected void subscribeActual(final Observer<? super App> observer)
            {

                Observable<JSONObject> ob = RequestManager.getInstance().getApp(id);
                ob.subscribe(new Consumer<JSONObject>() {
                                 @Override
                                 public void accept(@NonNull JSONObject jsonObject) throws Exception
                                 {
                                     try
                                     {
                                         App app = AppParser.getInstance().jsonToApp(jsonObject, "app");
                                         observer.onNext(app);
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
                                Log.d("MOTA--->","GET Error:"+throwable.getMessage());
                                observer.onError(throwable);
                            }
                        });
            }
        };
    }


    @Override
    public Observable<App> modifiy(final App app) {
        return new Observable<App>() {
            @Override
            protected void subscribeActual(final Observer<? super App> observer)
            {
                JSONObject appJson = AppParser.getInstance().appToJson(app);
                RequestManager.getInstance().updateApp(appJson).subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception
                    {
                        if(jsonObject.optBoolean("success"))
                        {
                            observer.onNext(app);
                            observer.onComplete();
                        }
                        else
                        {
                            observer.onNext(app);
                            observer.onComplete();
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

    @Override
    public Observable<Boolean> delete(final App app) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer)
            {
                JSONObject appId = new JSONObject();
                try {
                    appId.put("id",app.getId());
                    RequestManager.getInstance().deleteApp(appId).subscribe(new Consumer<JSONObject>() {
                        @Override
                        public void accept(@NonNull JSONObject jsonObject) throws Exception {
                            Log.d("MOTA--->","RESPUESTA?"+jsonObject.toString() + "OPT SUCCESS:"+jsonObject.optBoolean("success"));
                            if (jsonObject.optBoolean("success")) {
                                observer.onNext(true);
                                observer.onComplete();
                            } else {
                                observer.onNext(false);
                                observer.onComplete();
                            }
                            Log.d("MOTA--->","NO ENTRE EN THROW!");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception
                        {
                            Log.d("MOTA--->","ENTRE EN EL THROW?"+throwable.getMessage());
                            observer.onNext(false);
                            observer.onComplete();
                        }
                    });
                } catch (JSONException e)
                {
                    observer.onNext(false);
                    observer.onComplete();
                }
            }
        };
    }

    @Override
    public void updateApps(List<App> apps) {
        //
    }



}
