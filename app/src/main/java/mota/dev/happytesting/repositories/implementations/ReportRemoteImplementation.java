package mota.dev.happytesting.repositories.implementations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import mota.dev.happytesting.managers.RequestManager;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.repositories.ReportRepository;

/**
 * Created by user on 12/06/2017.
 */

public class ReportRemoteImplementation implements ReportRepository
{

    @Override
    public Observable<List<Report>> getAll()
    {
        return new Observable<List<Report>>()
        {
            @Override
            protected void subscribeActual(final Observer<? super List<Report>> observer)
            {
                RequestManager.getInstance().getReports().subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception
                    {
                        observer.onNext(generateReportList(jsonObject));
                        observer.onComplete();
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        observer.onError(throwable);
                    }
                });
            }
        };
    }

    @Override
    public Observable<Report> create(Report report) {
        return null;
    }

    @Override
    public Observable<Report> modifiy(Report report) {
        return null;
    }

    @Override
    public Observable<Boolean> delete(Report report) {
        return null;
    }


    private List<Report> generateReportList(JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.optJSONArray("reports");
        if (array.length() > 0)
        {
           List<Report> reports = new ArrayList<>();
            for (int i = 0; i<array.length(); i++)
                reports.add(generateReportFromJson(array.getJSONObject(i)));

            return reports;
        }
        return new ArrayList<>();
    }

    private Report generateReportFromJson(JSONObject jsonObject) throws JSONException
    {
        Report r = new Report();
        r.setName(jsonObject.optString("nombre"));
        r.setCreado(jsonObject.optString("createdAt"));
        r.setId(jsonObject.optInt("id"));
        r.setAppName(jsonObject.getJSONObject("App").optString("nombre"));
        return r;
    }
}
