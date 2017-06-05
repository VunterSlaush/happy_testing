package mota.dev.happytesting.repositories.implementations;

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
import mota.dev.happytesting.repositories.AppRepository;

/**
 * Created by Slaush on 29/05/2017.
 */

public class AppRemoteImplementation implements AppRepository {

    @Override
    public Observable<App> create(final String name)
    {
        Observable<App> response = new Observable<App>() {
            @Override
            protected void subscribeActual(final Observer<? super App> observer)
            {
                Observable<JSONObject> ob = RequestManager.getInstance().createApp(name);
                ob.subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception
                    {
                        try
                        {
                            App app = jsonToApp(jsonObject);
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

        return response;
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
                                         observer.onNext(getAppsOfJson(jsonObject));
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


    private App jsonToApp(JSONObject jsonObject) throws Exception
    {
        if(jsonObject.has("error"))
            throw new Exception();
        JSONObject appJson = jsonObject.getJSONObject("res");
        App app = new App();
        app.setId(appJson.optInt("id"));
        app.setName(appJson.optString("name"));
        return app;
    }


    private List<App> getAppsOfJson(JSONObject jsonObject) throws Exception
    {
        if(jsonObject.has("error"))
            throw new Exception();
        JSONArray array = jsonObject.getJSONArray("apps");
        List<App> apps = new ArrayList<>();
        for (int i = 0; i< array.length(); i++)
        {
            App app = new App();
            app.setName(array.getJSONObject(i).optString(Consts.NAME));
            app.setId(array.getJSONObject(i).optInt("id"));
            apps.add(app);
        }
        return apps;
    }
}
