package mota.dev.happytesting.ViewModel;

import android.content.Context;
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
import mota.dev.happytesting.managers.UserManager;

/**
 * Created by Slaush on 22/05/2017.
 */

public class LoginViewModel extends Observable
{


    public ObservableField<String> username;
    public ObservableField<String> password;
    private Context context;
    private UserManager userManager;

    public LoginViewModel(@NonNull Context context)
    {
        this.context = context;
        this.username = new ObservableField<>("");
        this.password = new ObservableField<>("");
        userManager = UserManager.getInstance(context);
        userManager.tryAutoLogin();
    }

    public void onClickLogin(View view)
    {
        userManager.login(username.get(),password.get());
    }

}
