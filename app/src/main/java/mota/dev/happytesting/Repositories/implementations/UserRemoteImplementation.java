package mota.dev.happytesting.repositories.implementations;

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
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.UserRepository;
import mota.dev.happytesting.managers.ErrorManager;
import mota.dev.happytesting.managers.RequestManager;

/**
 * Created by Slaush on 22/05/2017.
 */

public class UserRemoteImplementation implements UserRepository
{
    @Override
    public Observable<User> login(final String username, final String password) {
        Observable<User> observable = new Observable<User>() {
            @Override
            protected void subscribeActual(final Observer<? super User> observer)
            {
                final Observable<JSONObject> loginObservable = RequestManager.getInstance().login(username, password);
                loginObservable.subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JSONObject jsonObject) {
                        if(jsonObject.has("id"))
                        {
                            User user = createUserFromJSON(jsonObject);
                            observer.onNext(user);
                            observer.onComplete();
                        }
                        else
                        {
                            Throwable throwable = new Throwable("Credenciales Invalidas");
                            observer.onError(throwable);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e)
                    {
                       observer.onError(ErrorManager.getInstance().handleLoginError(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

            }
        };

        return observable;
    }

    @Override
    public Observable<List<User>> getUsers()
    {
        Observable<List<User>> observable = new Observable<List<User>>() {
            @Override
            protected void subscribeActual(final Observer<? super List<User>> observer)
            {
                Observable<JSONObject> users = RequestManager.getInstance().getUsers();

                users.subscribe(new Consumer<JSONObject>()
                {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception
                    {
                        observer.onNext(generateListOfUsers(jsonObject));
                        observer.onComplete();
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        observer.onError(throwable);
                    }
                });
            }
        };
        return  observable;
    }

    private List<User> generateListOfUsers(JSONObject jsonObject) throws Exception
    {
        ArrayList<User> users = new ArrayList<>();
        JSONArray array = jsonObject.optJSONArray("users");
        for (int i = 0; i <array.length(); i++)
            users.add(createUserFromJSON(array.getJSONObject(i)));

        return users;
    }

    private User createUserFromJSON(JSONObject jsonObject)
    {
        User user = new User();
        user.setId(jsonObject.optInt(Consts.USER_ID));
        user.setName(jsonObject.optString(Consts.NAME));
        user.setUsername(jsonObject.optString(Consts.USERNAME));
        return user;
    }
}
