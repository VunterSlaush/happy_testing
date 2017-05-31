package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.useCases.GetApps;

/**
 * Created by Slaush on 28/05/2017.
 */

public class AppsViewModel extends Observable
{
    private Context context;
    private List<App> apps;
    private GetApps useCase;
    public AppsViewModel(Context context)
    {
        this.context = context;
        apps = new ArrayList<>();
        useCase = new GetApps();
        fetchApps();
    }

    public List<App> getAppList()
    {
        return apps;
    }

    public void refresh()
    {
        fetchApps();
    }

    private void fetchApps()
    {
        useCase.fetchAllApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<App>>() {
                    @Override
                    public void accept(@NonNull List<App> apps) throws Exception
                    {
                        Log.d("APPS","ViewModel size:"+apps.size());
                        changeAppsDate(apps);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        apps.clear();
                        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void changeAppsDate(List<App> apps)
    {
        for (App app : apps)
        {
            if(!this.apps.contains(app))
                this.apps.add(app);
        }
        Log.d("APPS","SIZE ON CHANGE:"+this.apps.size());
        Log.d("APPS","LISTA"+apps.toString());
        setChanged();
        notifyObservers();
    }
}
