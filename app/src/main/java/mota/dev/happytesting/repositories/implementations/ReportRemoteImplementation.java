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
import mota.dev.happytesting.managers.UserManager;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;
import mota.dev.happytesting.parsers.ReportParser;
import mota.dev.happytesting.repositories.ReportRepository;
import mota.dev.happytesting.utils.RxHelper;

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
                        List<Report> reports = ReportParser.getInstance().generateReportList(jsonObject);
                        observer.onNext(reports);
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
    public Observable<List<Report>> getAppReports(App app) {
        return null;
    }

    @Override
    public Observable<Report> get(final int id, String name) {
        return new Observable<Report>() {
            @Override
            protected void subscribeActual(final Observer<? super Report> observer)
            {
                RequestManager.getInstance().getReport(id).subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(@NonNull JSONObject jsonObject) throws Exception
                    {
                        observer.onNext(ReportParser.getInstance()
                                .generateReportFromJson(jsonObject.optJSONObject("report")));
                        observer.onComplete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        observer.onError(throwable);
                    }
                });
            }
        };
    }

    @Override // TODO!!!
    public Observable<Report> create(Report report) {
        return null;
    }

    @Override // TODO!!!
    public Observable<Report> modifiy(Report report) {
        return null;
    }

    @Override
    public Observable<Boolean> delete(final Report report) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(final Observer<? super Boolean> observer)
            {
                JSONObject obj = new JSONObject();
                try
                {
                    obj.put("id",report.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestManager.getInstance()
                              .deleteReport(obj)
                              .subscribe(RxHelper.getSuccessConsumer(observer),
                                         RxHelper.getErrorThrowable(observer,false));
            }
        };
    }

}
