package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.Observable;

import mota.dev.happytesting.useCases.Login;

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
