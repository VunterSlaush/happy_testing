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
import mota.dev.happytesting.utils.Pnotify;

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
                        changeAppsData(apps);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        apps.clear();
                        Pnotify.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG,Pnotify.ERROR).show();
                    }
                });
    }

    private void changeAppsData(List<App> apps)
    {
        this.apps.clear();
        this.apps.addAll(apps);
        setChanged();
        notifyObservers();
    }
}
