package mota.dev.happytesting.Views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.R;
import mota.dev.happytesting.Views.adapters.UserAdapter;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.UserRepository;
import mota.dev.happytesting.repositories.implementations.UserRemoteImplementation;
import mota.dev.happytesting.utils.Pnotify;

/**
 * Created by Slaush on 23/06/2017.
 */

public class SelectUsersDialog
{
    private Context context;
    private AlertDialog dialog;
    private UserAdapter adapter;
    private List<User> selected;
    private OnGetUsers onGet;
    public SelectUsersDialog(Context contxt, List<User> selected, OnGetUsers onGet)
    {
        this.context = contxt;
        this.selected = selected;
        this.onGet = onGet;
        getUsers();
    }

    private void getUsers()
    {
        UserRepository repository = new UserRemoteImplementation();
        repository.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>()
                {
                    @Override
                    public void accept(@NonNull List<User> users) throws Exception
                    {
                        build(users);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Pnotify.makeText(context,"No se pudieron consultar los usuarios",Toast.LENGTH_LONG,Pnotify.ERROR).show();
                    }
                });
    }

    private void build(List<User> users)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Selecciona los Editores");

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.user_list, null);

        builder.setView(viewInflated);

        final UserAdapter adapter = new UserAdapter();
        RecyclerView userList = (RecyclerView) viewInflated.findViewById(R.id.userList);
        userList.setLayoutManager(new LinearLayoutManager(context));
        userList.setAdapter(adapter);
        adapter.setList(users);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onGet.get(adapter.getSelectedUsers());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // esto podria arreglarse! FIX IT!
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setSelectedUsers(selected);
            }
        },100);
        builder.show();
    }


    public interface OnGetUsers
    {
        void get(List<User> users);
    }

}
