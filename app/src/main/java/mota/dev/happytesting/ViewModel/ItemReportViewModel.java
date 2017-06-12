package mota.dev.happytesting.ViewModel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;

/**
 * Created by Slaush on 30/05/2017.
 */

public class ItemReportViewModel extends Observable {

    public ObservableInt reportId;
    public ObservableField<String> appName;
    public ObservableField<String> name;
    public ObservableField<String> sendText;
    public ObservableBoolean enableButton;
    public ObservableField<String> creado;
    private Context context;

    public ItemReportViewModel(Report report, Context context)
    {
        this.context = context;
        reportId = new ObservableInt(report.getId());
        appName = new ObservableField<>(report.getAppName());
        name = new ObservableField<>(report.getName());
        creado = new ObservableField<>(report.getCreado());
        sendText = new ObservableField<>("Enviar");
        enableButton = new ObservableBoolean(true);
    }

    public void setReport(Report report)
    {
        appName.set(report.getAppName());
        name.set(report.getName());
        creado.set(report.getCreado());
    }

    public void enviar(View view)
    {
        sendText.set("Enviando");
        enableButton.set(false);
    }

    public void abrir(View view)
    {
    }
}
