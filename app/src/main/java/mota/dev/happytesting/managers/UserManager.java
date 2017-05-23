package mota.dev.happytesting.managers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Consts;
import mota.dev.happytesting.MainActivity;
import mota.dev.happytesting.Models.User;
import mota.dev.happytesting.Repositories.UserRepository;
import mota.dev.happytesting.Repositories.implementations.UserRequestImplementation;
import mota.dev.happytesting.utils.PreferencesHelper;

/**
 * Created by Slaush on 22/05/2017.
 */

public class UserManager
{
    private static UserManager instance;
    private UserRepository repository;
    private Context context;

    private UserManager(Context context)
    {
        repository = new UserRequestImplementation();
        this.context = context;
    }

    public static UserManager getInstance(Context context)
    {
        if(instance == null || !instance.context.equals(context))
            instance = new UserManager(context);
        return  instance;
    }

    private void loginSuccess(Context context, User user)
    {
        saveUserCredentials(context,user);
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    private void saveUserCredentials(Context context,User user)
    {
        PreferencesHelper.writeString(context, Consts.USERNAME, user.getUsername());
        PreferencesHelper.writeString(context, Consts.PASSWORD, user.getPassword());
        PreferencesHelper.writeString(context, Consts.NAME, user.getName());
        PreferencesHelper.writeInteger(context, Consts.USER_ID, user.getId());
    }

    public User getUserIfExist(Context context)
    {
        User user = new User();
        String username = PreferencesHelper.readString(context,Consts.USERNAME,"");
        String password = PreferencesHelper.readString(context,Consts.PASSWORD,"");
        user.setUsername(username);
        user.setPassword(password);
        if(!username.isEmpty() && !password.isEmpty())
            return user;
        else
            return null;
    }

    public void login(final String username, final String password)
    {
        repository.login(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull User user)
                    {
                        user.setUsername(username);
                        user.setPassword(password);
                        loginSuccess(context,user);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e)
                    {
                        VolleyError error = (VolleyError)e;
                        Log.d(getClass().getName(),"ERROR:"+error.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void tryAutoLogin()
    {
        User user = getUserIfExist(context);
        if(RouterManager.getInstance().isConnected() && user != null)
        {
            login(user.getUsername(),user.getPassword());
        }
        else if(user != null)
        {
            loginSuccess(context,user);
        }
    }
}
