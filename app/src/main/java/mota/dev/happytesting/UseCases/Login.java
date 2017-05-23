package mota.dev.happytesting.UseCases;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.MainActivity;
import mota.dev.happytesting.Models.User;
import mota.dev.happytesting.Repositories.UserRepository;
import mota.dev.happytesting.Repositories.implementations.UserRequestImplementation;
import mota.dev.happytesting.managers.RouterManager;
import mota.dev.happytesting.managers.UserManager;

/**
 * Created by user on 23/05/2017.
 */

public class Login
{
    private UserRepository repository;
    private Context context;
    private String username;
    private String password;
    private LoginInterface loginInterface;

    private Observer<User> userObserver = new Observer<User>()
    {
        @Override
        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d)
        {

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
            loginInterface.onError(e.getMessage());
        }

        @Override
        public void onComplete()
        {

        }
    };

    public Login(Context context, LoginInterface loginInterface)
    {
        repository = new UserRequestImplementation();
        this.context = context;
        this.loginInterface = loginInterface;
    }

    public void login(final String username, final String password)
    {
        this.username = username;
        this.password = password;

        repository.login(username,password)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(userObserver);
    }

    public void tryAutoLogin()
    {
        User user = UserManager.getInstance().getUserIfExist(context);

        if(RouterManager.getInstance().isConnected() && user != null)
            login(user.getUsername(),user.getPassword());
        else if(user != null)
            loginSuccess(context,user);
    }

    private void loginSuccess(Context context, User user)
    {
        UserManager.getInstance().saveUserCredentials(context,user);
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    public interface LoginInterface
    {
        void onError(String errorMessage);
    }

}
