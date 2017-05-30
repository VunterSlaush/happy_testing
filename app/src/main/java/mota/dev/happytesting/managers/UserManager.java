package mota.dev.happytesting.managers;

import android.content.Context;

import mota.dev.happytesting.Consts;
import mota.dev.happytesting.MyApplication;
import mota.dev.happytesting.models.User;
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

    public String getUserId()
    {
        return PreferencesHelper.readString(MyApplication.getInstance(),Consts.USER_ID,"");
    }

    public void logout() // esto es todo lo que hay que borrar para cerrar la sesion!
    {
        PreferencesHelper.deleteKey(MyApplication.getInstance(),Consts.USERNAME);
        PreferencesHelper.deleteKey(MyApplication.getInstance(),Consts.PASSWORD);
        PreferencesHelper.deleteKey(MyApplication.getInstance(), Consts.NAME);
        PreferencesHelper.deleteKey(MyApplication.getInstance(), Consts.USER_ID);
    }
}
