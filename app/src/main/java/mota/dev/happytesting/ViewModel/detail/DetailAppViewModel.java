package mota.dev.happytesting.ViewModel.detail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
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
import io.realm.RealmList;
import mota.dev.happytesting.Views.dialogs.SelectUsersDialog;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.useCases.AppDetail;
import mota.dev.happytesting.useCases.DeleteApp;
import mota.dev.happytesting.useCases.EditEditors;
import mota.dev.happytesting.utils.Functions;

/**
 * Created by Slaush on 12/06/2017.
 */

public class DetailAppViewModel extends Observable {

    private Context context;
    private List<Report> reports;
    private List<String> editors;
    public ObservableField<String> app_name;
    public ObservableField<String> app_owner;
    private ObservableInt app_id;
    private AppDetail useCase;
    private App app;

    public DetailAppViewModel(Context context)
    {
        this.context = context;
        this.app_name = new ObservableField<>();
        this.app_owner = new ObservableField<>();
        this.app_id = new ObservableInt();
        this.useCase = new AppDetail();
    }

    //TODO recibir app name por si la app no esta en server !!!
    public void setApp(int id)
    {
        Log.d("MOTA--->","App Id:"+id);
        app_id.set(id);
        findAppDetails(id);
    }

    private void findAppDetails(int id)
    {
        useCase.getAppDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<App>() {
            @Override
            public void accept(@NonNull App app) throws Exception
            {
                setApp(app);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                // Do Nothing ..
                Log.d("MOTA--->","ERROR:"+throwable.getMessage());
            }
        });
    }

    private void setApp(App app)
    {
        this.app = app;
        app_name.set(app.getName());
        if(app.getApp_owner() != null)
        app_owner.set("@"+app.getApp_owner().getUsername()+" - "+app.getApp_owner().getName());
        editors = convertirEditorsToStringList(app.getModificar());
        reports = app.getReports();
        if(reports != null)
        setChanged();
        notifyObservers();

    }

    private List<String> convertirEditorsToStringList(RealmList<User> modificar)
    {
        ArrayList<String> editors = new ArrayList<>();
        for (User u: modificar)
        {
            editors.add("@"+u.getUsername()+" - "+u.getName());
        }
        return editors;
    }

    public void eliminarApp(View view)
    {
        Functions.showConfirmDialog(context, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                DeleteApp useCase = new DeleteApp();
                useCase.delete(app).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean result) throws Exception {
                        if(result)
                        {
                            Toast.makeText(context,"Aplicacion Eliminada Satisfactoriamente", Toast.LENGTH_LONG).show();
                            finalizar();
                        }
                        else
                        {
                            Toast.makeText(context,"La Aplicacion no pudo ser Eliminada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void finalizar()
    {
        ((Activity)context).finish();
    }

    public void agregarEditor(View view)
    {
        SelectUsersDialog dialog = new SelectUsersDialog(context, app.getModificar(), new SelectUsersDialog.OnGetUsers() {
            @Override
            public void get(List<User> users)
            {
                app.setModificar(users);
                EditEditors useCase = new EditEditors();
                useCase.edit(app).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>()
                {
                    @Override
                    public void accept(@NonNull Boolean result) throws Exception
                    {
                        if (result)
                            Toast.makeText(context,"Editores cambiados satisfactoriamente",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context,"Ocurrio un Error al cambiar los editores",Toast.LENGTH_SHORT).show();
                        setApp(app);
                    }
                });
            }

        });
    }

    public void agregarReporte(View view)
    {
        //TODO!
    }

    public List<Report> getReports()
    {
        return reports;
    }

    public void refreshReports()
    {
        // TODO ????
    }

    public List<String> getEditors() {
        return editors;
    }
}
