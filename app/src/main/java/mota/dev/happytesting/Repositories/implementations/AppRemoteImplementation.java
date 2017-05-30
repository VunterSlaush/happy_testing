package mota.dev.happytesting.repositories.implementations;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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
                ob.subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JSONObject jsonObject)
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

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        };

        return response;
    }

    @Override
    public Observable<List<App>> getAll() {
        return null;
    }

    @Override
    public Observable<App> modifiy(App app) {
        return null;
    }

    @Override
    public Observable<Boolean> delete(App app) {
        return null;
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
}
