package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Views.adapters.UserAdapter;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.UserRepository;
import mota.dev.happytesting.repositories.implementations.UserRemoteImplementation;
import mota.dev.happytesting.useCases.CreateApp;

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
        useCase = new CreateApp(context);
        repository = new UserRemoteImplementation();
        users = new ArrayList<>();
        adapter = new UserAdapter();
        refresh();
    }

    public void create(View view)
    {

        List<User> selected = adapter.getSelectedUsers();
        Log.d("MOTA--->", "Selected Size:"+selected.size());
        useCase.createApp(name.get(),selected);
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
                Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();
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
