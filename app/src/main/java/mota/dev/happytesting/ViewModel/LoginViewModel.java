package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Models.User;
import mota.dev.happytesting.Repositories.UserRepository;
import mota.dev.happytesting.Repositories.implementations.UserRequestImplementation;
import mota.dev.happytesting.UseCases.Login;
import mota.dev.happytesting.managers.UserManager;

/**
 * Created by Slaush on 22/05/2017.
 */

public class LoginViewModel extends Observable
{
    public  ObservableField<String> username;
    public  ObservableField<String> password;
    public  ObservableField<String> error;
    private Context context;
    private Login loginUseCase;

    private Login.LoginInterface loginInterface =  new Login.LoginInterface()
    {
        @Override
        public void onError(String errorMessage)
        {
            error.set(errorMessage);
        }
    };

    public LoginViewModel(@NonNull Context context)
    {
        this.context = context;
        this.username = new ObservableField<>("");
        this.password = new ObservableField<>("");
        this.error    = new ObservableField<>("");

        loginUseCase = new Login(context, loginInterface);
        loginUseCase.tryAutoLogin();
    }

    public void onClickLogin(View view)
    {
        loginUseCase.login(username.get(),password.get());
    }

}
