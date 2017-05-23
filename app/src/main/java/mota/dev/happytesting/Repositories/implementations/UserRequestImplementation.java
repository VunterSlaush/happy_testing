package mota.dev.happytesting.Repositories.implementations;


import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.Consts;
import mota.dev.happytesting.Models.User;
import mota.dev.happytesting.Repositories.UserRepository;
import mota.dev.happytesting.managers.RequestManager;

/**
 * Created by Slaush on 22/05/2017.
 */

public class UserRequestImplementation implements UserRepository
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
                        }else
                            observer.onError(new Exception("User Not Found"));
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

        return observable;
    }

    private User createUserFromJSON(JSONObject jsonObject)
    {
        User user = new User();
        user.setId(jsonObject.optInt(Consts.USER_ID));
        user.setName(jsonObject.optString(Consts.NAME));
        return user;
    }
}
