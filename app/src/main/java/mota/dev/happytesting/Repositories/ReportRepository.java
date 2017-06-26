package mota.dev.happytesting.repositories;

import java.util.List;

import io.reactivex.Observable;
import mota.dev.happytesting.models.App;
import mota.dev.happytesting.models.Report;

/**
 * Created by Slaush on 11/06/2017.
 */

public interface ReportRepository
{
    Observable<List<Report>> getAll();
    Observable<List<Report>> getAppReports(App app);
    Observable<Report> get(int id, String name);
    Observable<Report> create(Report report);
    Observable<Report> modifiy(Report report);
    Observable<Boolean> delete(Report report);
}
