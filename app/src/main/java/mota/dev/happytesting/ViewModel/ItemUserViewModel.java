package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.util.Observable;

import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.User;

/**
 * Created by Slaush on 11/06/2017.
 */

public class ItemUserViewModel extends Observable
{
    public ObservableField<String> name;
    public ObservableField<String> username;
    public ObservableBoolean checked;
    private User user;
    private Context context;

    public ItemUserViewModel(User user, Context context)
    {
        this.context = context;
        name = new ObservableField<>(user.getName());
        username = new ObservableField<>(user.getUsername());
        checked = new ObservableBoolean(false);
        this.user = user;
    }

    public void setUser(User user)
    {
        this.user = user;
        username.set(user.getUsername());
        name.set(user.getName());
        checked.set(false);
    }
}
