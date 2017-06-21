package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.useCases.UpdateAccount;

/**
 * Created by Slaush on 20/06/2017.
 */

public class AccountViewModel extends Observable
{
    private Context context;
    private UpdateAccount useCase;

    public ObservableField<String> name;
    public ObservableField<String> username;
    public ObservableField<String> password;
    public ObservableField<String> confirm_password;

    public AccountViewModel(Context context)
    {
        this.context = context;
        name = new ObservableField<>();
        username = new ObservableField<>();
        password = new ObservableField<>();
        confirm_password = new ObservableField<>();
        useCase = new UpdateAccount();
        putUserData(useCase.getUserData());
    }

    private void putUserData(User u)
    {
        name.set(u.getName());
        username.set(u.getUsername());
        password.set(u.getPassword());
        confirm_password.set(u.getPassword());
    }

    public void updateUserData(View view) // TODO refactorizar!
    {

        if(password.get().trim().isEmpty() || confirm_password.get().trim().isEmpty() ||
                name.get().trim().isEmpty() || username.get().trim().isEmpty())
        {
            putError("Ningun Campo puede estar vacio");
            return;
        }



        if(password.get().equals(confirm_password.get()))
            callUpdateUserData(view);
        else
            putError("Las Contraseñas no Coinciden");

    }

    public void putError(String error)
    {
        Toast.makeText(context,error,Toast.LENGTH_LONG).show();
    }

    private void callUpdateUserData(final View view)
    {
        view.setEnabled(false);
        useCase.updateUserData(name.get(), username.get(),password.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
            @Override
            public void accept(@NonNull User user) throws Exception
            {
                putUserData(user);
                putError("Datos Actualizados Satisfactoriamente!");// esto sera un Success!
                view.setEnabled(true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                putError(throwable.getMessage());
                view.setEnabled(true);
            }
        });
    }
}
