package mota.dev.happytesting.ViewModel.detail;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mota.dev.happytesting.Views.activities.DetailReportActivity;
import mota.dev.happytesting.models.Observation;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.useCases.ReportDetail;

/**
 * Created by Slaush on 18/06/2017.
 */

public class DetailReportViewModel extends Observable {

    private Context context;
    private List<Observation> observations;
    private ObservableInt reportId;
    private ObservableField<String> reportName;
    private ObservableField<String> appName;
    private ReportDetail detailUseCase;

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
        //TODO!!!
    }

    public void agregarObservacion(View view)
    {
        //TODO!!!
    }

    public void publicar(View view)
    {
        //TODO!!
    }

    public void setReportData(int report_id, String report_name)
    {
        reportId.set(report_id);
        reportName.set(report_name);
        detailUseCase.getDetails(report_id).subscribeOn(Schedulers.io())
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
        setChanged();
        notifyObservers();
    }

    public List<Observation> getObservations()
    {
        return observations;
    }
}
