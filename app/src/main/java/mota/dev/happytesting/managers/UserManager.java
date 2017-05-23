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

    private UserManager()
    {

    }

    public static UserManager getInstance()
    {
        if(instance == null )
            instance = new UserManager();
        return  instance;
    }


    public void saveUserCredentials(Context context,User user)
    {
        PreferencesHelper.writeString(context, Consts.USERNAME, user.getUsername());
        PreferencesHelper.writeString(context, Consts.PASSWORD, user.getPassword());
        PreferencesHelper.writeString(context, Consts.NAME, user.getName());
        PreferencesHelper.writeInteger(context, Consts.USER_ID, user.getId());
    }

    public User getUserIfExist(Context context)
    {
        String username = PreferencesHelper.readString(context,Consts.USERNAME,"");
        String password = PreferencesHelper.readString(context,Consts.PASSWORD,"");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        if(!username.isEmpty() && !password.isEmpty())
            return user;
        else
            return null;
    }


}
