package mota.dev.happytesting.ViewModel.detail;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.useCases.AppDetail;

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

    public DetailAppViewModel(Context context)
    {
        this.context = context;
        this.app_name = new ObservableField<>();
        this.app_owner = new ObservableField<>();
        this.app_id = new ObservableInt();
        this.useCase = new AppDetail();
    }

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
        Log.d("MOTA--->","Entre en el SET!");
        app_name.set(app.getName());
        if(app.getApp_owner() != null)
        app_owner.set("@"+app.getApp_owner().getUsername()+" - "+app.getApp_owner().getName());
        editors = convertirEditorsToStringList(app.getModificar());
        reports = app.getReports();
        if(reports != null)
        Log.d("MOTA--->","Seteando Apps R:"+reports.size()+" E:"+editors.size());
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

    }

    public void agregarEditor(View view)
    {

    }

    public void agregarReporte(View view)
    {

    }

    public List<Report> getReports()
    {
        return reports;
    }

    public void refreshReports()
    {

    }

    public List<String> getEditors() {
        return editors;
    }
}
