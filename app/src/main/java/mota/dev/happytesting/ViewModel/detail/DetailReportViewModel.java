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
import mota.dev.happytesting.Views.activities.DetailReportActivity;
import mota.dev.happytesting.Views.dialogs.SimpleInputDialog;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.useCases.CreateObservation;
import mota.dev.happytesting.useCases.DeleteReport;
import mota.dev.happytesting.useCases.ReportDetail;
import mota.dev.happytesting.utils.Functions;

/**
 * Created by Slaush on 18/06/2017.
 */

public class DetailReportViewModel extends Observable {

    private Context context;
    private List<Observation> observations;
    private ObservableInt reportId;
    public ObservableField<String> reportName;
    public ObservableField<String> appName;
    private ReportDetail detailUseCase;
    private Report report;

    public DetailReportViewModel(Context context)
    {
        this.context = context;
        observations = new ArrayList<>();
        reportId = new ObservableInt();
        reportName = new ObservableField<>();
        appName = new ObservableField<>();
        detailUseCase = new ReportDetail();
    }

    public void enviar(View view)
    {
        //TODO!!!
    }

    public void eliminar(View view)
    {
        Functions.showConfirmDialog(context, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                DeleteReport useCase = new DeleteReport();
                useCase.delete(report).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean result) throws Exception
                    {
                        if (result)
                        {
                            Toast.makeText(context,"Eliminado Satisfactoriamente", Toast.LENGTH_SHORT).show();
                            ((Activity)context).finish();
                        }
                        else
                        {
                            Toast.makeText(context,"El reporte no pudo ser eliminado", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void agregarObservacion(View view)
    {
        new SimpleInputDialog(context, "Ingrese observacion", new SimpleInputDialog.OnGetText() {
            @Override
            public void get(String text) {
                CreateObservation.getInstance().create(report,text)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>()
                {
                    @Override
                    public void accept(@NonNull Boolean result) throws Exception
                    {
                        if(result)
                            updateReportData(report);
                        else
                            Toast.makeText(context,"La Observacion no pudo ser creada",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void publicar(View view)
    {
        //TODO!!
    }

    public void setReportData(int report_id, String report_name)
    {
        reportId.set(report_id);
        reportName.set(report_name);
        detailUseCase.getDetails(report_id,report_name).subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Report>()
        {
            @Override
            public void accept(@NonNull Report report) throws Exception
            {
                updateReportData(report);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                Toast.makeText(context,"Ocurrio un error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateReportData(Report report)
    {
        reportId.set(report.getId());
        reportName.set(report.getName());
        observations.clear();
        observations.addAll(report.getObservations());
        appName.set(report.getAppName());
        this.report = report;
        setChanged();
        notifyObservers();
    }

    public List<Observation> getObservations()
    {
        return observations;
    }
}
