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
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.useCases.GetApps;
import mota.dev.happytesting.useCases.GetReports;

/**
 * Created by Slaush on 28/05/2017.
 */

public class ReportsViewModel extends Observable
{
    private Context context;
    private List<Report> reports;
    private GetReports useCase;

    public ReportsViewModel(Context context)
    {
        this.context = context;
        reports = new ArrayList<>();
        useCase = new GetReports();
        fetchReports();
    }

    public List<Report> getReportList()
    {
        return reports;
    }

    public void refresh()
    {
        fetchReports();
    }

    private void fetchReports()
    {
        useCase.fetchReports()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Report>>() {
                    @Override
                    public void accept(@NonNull List<Report> reports) throws Exception
                    {
                        changeReportData(reports);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        reports.clear();
                        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void changeReportData(List<Report> reports)
    {
        this.reports.clear();
        this.reports.addAll(reports);
        setChanged();
        notifyObservers();
    }
}
