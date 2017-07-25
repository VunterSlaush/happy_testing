package mota.dev.happytesting.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.MainActivity;
import mota.dev.happytesting.Views.adapters.UserAdapter;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.UserRepository;
import mota.dev.happytesting.repositories.implementations.UserRemoteImplementation;
import mota.dev.happytesting.useCases.CreateApp;
import mota.dev.happytesting.utils.Pnotify;

/**
 * Created by Slaush on 28/05/2017.
 */

public class CreateAppViewModel extends Observable
{
    private Context context;
    public ObservableField<String> name;
    private CreateApp useCase;
    private UserRepository repository;
    private List<User> users;
    private UserAdapter adapter;

    public CreateAppViewModel(Context context)
    {
        this.context = context;
        name = new ObservableField<>("");
        useCase = CreateApp.getInstance();
        repository = new UserRemoteImplementation();
        users = new ArrayList<>();
        adapter = new UserAdapter();
        refresh();
    }

    public void create(View view)
    {
        List<User> selected = adapter.getSelectedUsers();
        useCase.createApp(name.get(),selected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<App>() {
            @Override
            public void accept(@NonNull App app) throws Exception
            {
                onCreateAppSuccess(app);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Pnotify.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT,Pnotify.ERROR).show();
            }
        });
    }

    private void onCreateAppSuccess(App app)
    {
        if(app.getId() != -1)
            Pnotify.makeText(context,"Aplicacion Creada y enviada al Servidor", Toast.LENGTH_SHORT,Pnotify.INFO).show();
        else
            Pnotify.makeText(context,"La aplicacion fue creada, pero no enviada al servidor", Toast.LENGTH_SHORT,Pnotify.WARNING).show();

        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
        ((Activity) context).finish();
    }

    public void refresh()
    {
        repository.getUsers()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Consumer<List<User>>()
        {
            @Override
            public void accept(@NonNull List<User> users) throws Exception
            {
                changeUsers(users);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                users.clear();
                Pnotify.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG,Pnotify.ERROR).show();
            }
        });
    }

    private void changeUsers(List<User> users)
    {
        this.users.clear();
        this.users.addAll(users);
        setChanged();
        notifyObservers();
    }

    public List<User> getUsers()
    {
        return users;
    }

    public UserAdapter getAdapter()
    {
        return adapter;
    }
}
