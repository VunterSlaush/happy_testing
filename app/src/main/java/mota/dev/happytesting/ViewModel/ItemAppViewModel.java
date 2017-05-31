package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import java.util.Observable;

import mota.dev.happytesting.models.App;

/**
 * Created by Slaush on 30/05/2017.
 */

public class ItemAppViewModel extends Observable {

    public  ObservableInt appId;
    public ObservableField<String> appName;
    private Context context;

    public ItemAppViewModel(App app, Context context)
    {
        this.context = context;
        appId = new ObservableInt(app.getId());
        appName = new ObservableField<>(app.getName());
    }

    public void setApp(App app)
    {
        appId.set(app.getId());
        appName.set(app.getName());
    }
}
