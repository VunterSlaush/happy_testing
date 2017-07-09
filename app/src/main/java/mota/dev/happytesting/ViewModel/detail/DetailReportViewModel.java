package mota.dev.happytesting.ViewModel.detail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Views.activities.DetailReportActivity;
import mota.dev.happytesting.Views.dialogs.SimpleInputDialog;
import mota.dev.happytesting.managers.RequestManager;
import mota.dev.happytesting.managers.RouterManager;
import mota.dev.happytesting.models.Image;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.useCases.AddImages;
import mota.dev.happytesting.useCases.CreateObservation;
import mota.dev.happytesting.useCases.DeleteReport;
import mota.dev.happytesting.useCases.PublishReport;
import mota.dev.happytesting.useCases.ReportDetail;
import mota.dev.happytesting.useCases.SendReport;
import mota.dev.happytesting.utils.Functions;

/**
 * Created by Slaush on 18/06/2017.
 */

public class DetailReportViewModel extends Observable {

    private static final String TAG = DetailReportViewModel.class.getSimpleName();

    private Context context;
    private List<Observation> observations;
    public ObservableInt reportId;
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
        report.setObservations(observations);
        SendReport.getInstance().send(report)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Consumer<Report>() {
            @Override
            public void accept(@NonNull Report reportRes) throws Exception {
                updateReportData(reportRes);
                Toast.makeText(context,"Reporte Enviado Satisfactoriamente", Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Observation>() {
                    @Override
                    public void accept(@NonNull Observation result) throws Exception {
                        observations.add(result);
                        report.addObservation(result);
                        setChanged();
                        notifyObservers();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void publicar(final View view)
    {
        PublishReport.getInstance().publicarReporte(report).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
        {
            @Override
            public void accept(@NonNull final String s) throws Exception
            {
                Functions.showAskDialog(context,"¿Desea visualizar el reporte?",  new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        String url = RouterManager.getInstance().getUrlBase()+"/"+s;
                        Log.d(TAG,"Abriendo URL:"+url);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                });
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Snackbar.make(view,throwable.getMessage(),Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setReportData(int report_id, String report_name)
    {
        reportId.set(report_id);
        reportName.set(report_name);
        detailUseCase.getDetails(report_id,report_name)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Report>()
        {
            @Override
            public void accept(@NonNull Report report) throws Exception
            {
                if(report.getId() != -1)
                    updateReportData(report);
                else
                    findObservations(report);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception
            {
                Toast.makeText(context,"Ocurrio un error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findObservations(final Report report)
    {
        detailUseCase.findLocalObservations(report).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Observation>>() {
            @Override
            public void accept(@NonNull List<Observation> observations) throws Exception
            {
                report.setObservations(observations);
                updateReportData(report);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();
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

    public void onActivityResult(Bundle extras)
    {
        addImagesToCorrespondObservation(extras);
    }

    private void addImagesToCorrespondObservation(Bundle extras)
    {

        final String reportName = extras.getString("report_name");
        String data = extras.getString("data");
        int id = extras.getInt("observation_id");
        String localId = extras.getString("observation_local_id");
        Log.d("MOTA---->","Reporte>"+reportName + " VS "+report.getName());
        if(report.getName().equals(reportName))
        {
            List<Image> dataImages = Functions.generateImageListFromString(data);
            new AddImages()
                    .addImages(dataImages,id, localId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Observation>() {
                        @Override
                        public void accept(@NonNull final Observation result) throws Exception
                        {
                            int i = observations.indexOf(result);
                            if(i >= 0)
                            {
                                observations.remove(i);
                                observations.add(i,result);
                                setChanged();
                                notifyObservers();
                                Toast.makeText(context,"Imagenes Agregadas Satisfactoriamente", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(context,"No se pudieron Agregar las imagenes", Toast.LENGTH_SHORT).show();

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
