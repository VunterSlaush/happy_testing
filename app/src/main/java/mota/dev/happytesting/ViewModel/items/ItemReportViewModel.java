package mota.dev.happytesting.ViewModel.items;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Views.activities.DetailAppActivity;
import mota.dev.happytesting.Views.activities.DetailReportActivity;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.models.User;
import mota.dev.happytesting.repositories.AppRepository;
import mota.dev.happytesting.repositories.implementations.AppRemoteImplementation;
import mota.dev.happytesting.useCases.SendReport;
import mota.dev.happytesting.utils.Functions;
import mota.dev.happytesting.utils.Pnotify;

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
    private Report report;

    public ItemReportViewModel(Report report, Context context)
    {
        this.context = context;
        this.report = report;
        reportId = new ObservableInt(report.getId());
        appName = new ObservableField<>(report.getAppName());
        name = new ObservableField<>(report.getName());
        creado = new ObservableField<>(Functions.formatDate(report.getCreado()));
        sendText = new ObservableField<>("Enviar");
        enableButton = new ObservableBoolean(true);
    }

    public void setReport(Report report)
    {
        Log.i("MOTA--","REPORT ES NULL00??:"+ (report == null));
        this.report = report;
        reportId.set(report.getId());
        appName.set(report.getAppName());
        name.set(report.getName());
        creado.set(Functions.formatDate(report.getCreado()));
    }

    public void enviar(View view)
    {
        sendText.set("Enviando");
        enableButton.set(false);
        SendReport.getInstance().send(report)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Report>() {
                    @Override
                    public void accept(@NonNull Report reportRes) throws Exception
                    {
                        setReport(reportRes);
                        Pnotify.makeText(context,"Reporte Enviado Satisfactoriamente", Toast.LENGTH_SHORT,Pnotify.INFO).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        enableButton.set(true);
                        Pnotify.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT,Pnotify.ERROR).show();
                    }
                });
    }

    public void abrir(View view)
    {
        Intent i = new Intent(context, DetailReportActivity.class);
        i.putExtra("report_id",reportId.get());
        i.putExtra("report_name",name.get());
        context.startActivity(i);
    }
}
